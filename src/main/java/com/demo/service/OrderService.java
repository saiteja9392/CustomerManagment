package com.demo.service;

import com.demo.entity.*;
import com.demo.exception.custom.ResourceException;
import com.demo.model.OrderRequest;
import com.demo.repository.*;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.demo.enumaration.Status.DEBITED;
import static com.demo.enumaration.Status.DELIVERED;

@Service
public class OrderService {

	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	CustomerLoginRepo customerLoginRepo;

	@Autowired
	OrderRepo orderRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	WalletRepo walletRepo;

	@Autowired
	PromoCodeRepo promoCodeRepo;

	@Autowired
	WalletTransactionRepo walletTransactionRepo;

	public List<Order> getCustomerOrderDetails(String username) {
		
		System.out.println(orderRepo.count());

		Optional<Customer> c = customerRepo.findById(username);

		Optional<CustomerLogin> cLogin = customerLoginRepo.findById(username);

		if(!c.isPresent()|| !cLogin.isPresent())
			throw new ResourceException("No Login Found");
		
		List<Order> orders = orderRepo.findByUsername(username);

		Collections.sort(orders, Comparator.comparing(Order::getTransactionId).reversed());

		if(orders == null)
			throw new ResourceException("No Orders Found");
		
		return orders;
	}

	@Transactional
	public Response placeOrder(String username, OrderRequest orderRequest) {

		Optional<Customer> customerInfo = customerRepo.findById(username);
		Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(username);

		if(!customerInfo.isPresent()|| !customerLoginInfo.isPresent())
			throw new ResourceException("No Login Found");

		Optional<Product> findProduct = productRepo.findById(orderRequest.getProductId());

		if(!findProduct.isPresent())
			throw new ResourceException(String.format("No Product Found With Product Id - %s",orderRequest.getProductId()));

		if(findProduct.get().getQuantityInStore() == 0)
			throw new ResourceException("Out Of Stock!!!");

		if(orderRequest.getQuantity() > findProduct.get().getQuantityInStore())
			throw new ResourceException(String.format("Available Stock In Store - %s, Please Select As Per That",
					findProduct.get().getQuantityInStore()));

		Optional<Wallet> walletById = walletRepo.findById(username);

		if(!walletById.isPresent())
			throw new ResourceException("No Wallet Found For Customer Login");

		if (!walletById.get().getStatus())
			throw new ResourceException("Wallet Is Disabled!!! Please Enable And Place Order");

		if(walletById.get().getBalance() <= 0)
			throw new ResourceException("Insufficient Balance!!!");

		List<Order> orderByUsername = orderRepo.findByUsername(username);
		Optional<PromoCode> promo = Optional.empty();

		Response resp = null;
		if(orderRequest.getPromoCode() != null) {
			promo = promoCodeRepo.findById(orderRequest.getPromoCode());
		}

		if(orderRequest.getPromoCode() != null && !promo.isPresent())
			throw new ResourceException("Invalid Promo Code!!!");

		if(promo.isPresent()) {
			if (promo.get().getStatus().booleanValue() == false)
				throw new ResourceException("Promo Code Is Not Active!!!");
		}

		if(orderByUsername.size() > 0) {
			if (orderRequest.getPromoCode() != null && orderRequest.getPromoCode().toUpperCase().contentEquals("FIRSTBUY"))
				throw new ResourceException(String.format("'%s' Promo Code Is Valid On First Order!!!",orderRequest.getPromoCode().toUpperCase()));

			else {
				resp = this.orderProduct(findProduct,promo,walletById,orderRequest,username);
			}
		}
		else{
			resp = this.orderProduct(findProduct,promo,walletById,orderRequest,username);
		}

		return resp;
	}

	@Transactional
	private Response orderProduct(Optional<Product> findProduct, Optional<PromoCode> promo, Optional<Wallet> walletById,
								  OrderRequest orderRequest, String username) {

		int totalPrice = findProduct.get().getPrice() * orderRequest.getQuantity();

		int offerMoney = 0;
		if (findProduct.get().getOffer() != null) {

			offerMoney = ((totalPrice * findProduct.get().getOffer().getPercentage()) / 100);
			totalPrice = totalPrice - offerMoney;
		}

		int promoAmount = 0;

		if (promo.isPresent()) {
			if (totalPrice <= promo.get().getAmount())
				throw new ResourceException("Total Amount Must Be Greater Than Promo Code Amount!!!");
			else
				promoAmount = promo.get().getAmount();
		}

		totalPrice = totalPrice - promoAmount;

		if (walletById.get().getBalance() < totalPrice)
			throw new ResourceException("Insufficient Balance!!!");

		Wallet w = new Wallet();

		w.setWalletId(username);
		w.setBalance(walletById.get().getBalance() - totalPrice);
		w.setStatus(walletById.get().getStatus());

		walletRepo.save(w);

		findProduct.get().setQuantityInStore(findProduct.get().getQuantityInStore() - orderRequest.getQuantity());
		productRepo.save(findProduct.get());

		Order order = new Order();

		order.setUsername(username);
		order.setProduct(findProduct.get().getProductName());
		order.setQuantity(orderRequest.getQuantity());
		order.setProductPrice(findProduct.get().getPrice());
		order.setOfferAmount(offerMoney);
		order.setPromoAmount(promoAmount);
		order.setFinalPrice(totalPrice);
		order.setDateOfPurchase(new Date());
		order.setTransactionId(order.getTransactionId());
		order.setStatus(DELIVERED.name());

		String offerApplied = null;

		if (findProduct.get().getOffer() != null)
			offerApplied = findProduct.get().getOffer().getOfferId().toUpperCase();

		if (promo.isPresent()) {
			if (offerApplied == null)
				offerApplied = promo.get().getCode().toUpperCase();
			else
				offerApplied = offerApplied + "," + promo.get().getCode().toUpperCase();
		}

		if (offerApplied != null)
			order.setOffersApplied(offerApplied);

		Order savedOrder = orderRepo.save(order);

		addToWalletTransaction(username, totalPrice,savedOrder);

		Response resp = Response.buildResponse("Order Placed Successfully!!!", savedOrder);

		return resp;
	}

	@Transactional
	private WalletTransaction addToWalletTransaction(String username, int totalPrice, Order order) {

		WalletTransaction walletTransaction = new WalletTransaction();
		walletTransaction.setTransactionId(walletTransaction.getTransactionId());
		walletTransaction.setTransactionType(DEBITED.name());
		walletTransaction.setAmount(totalPrice);
		walletTransaction.setLoginId(username);
		walletTransaction.setReferenceId(order.getTransactionId());

		return walletTransactionRepo.save(walletTransaction);
	}

	public Optional<Order> getOrderByTransactionId(String transactionId) {

		return orderRepo.findById(transactionId);

	}
}
