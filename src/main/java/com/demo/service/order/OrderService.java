package com.demo.service.order;

import com.demo.entity.Product;
import com.demo.entity.PromoCode;
import com.demo.entity.order.Cart;
import com.demo.entity.order.Order;
import com.demo.entity.order.OrderSummary;
import com.demo.entity.wallet.Wallet;
import com.demo.entity.wallet.WalletTransaction;
import com.demo.exception.custom.ResourceException;
import com.demo.model.order.OrderRequest;
import com.demo.model.order.PlacedOrder;
import com.demo.repository.ProductRepo;
import com.demo.repository.PromoCodeRepo;
import com.demo.repository.order.CartRepo;
import com.demo.repository.order.OrderRepo;
import com.demo.repository.order.OrderSummaryRepo;
import com.demo.repository.wallet.WalletRepo;
import com.demo.repository.wallet.WalletTransactionRepo;
import com.demo.response.Response;
import com.demo.validation.CustomerValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.demo.enumaration.Status.DEBITED;
import static com.demo.enumaration.Status.DELIVERED;

@Service
public class OrderService {
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

	@Autowired
	CartRepo cartRepo;

	@Autowired
	OrderSummaryRepo orderSummaryRepo;

	@Autowired
	CustomerValidation customerValidation;

	public List<OrderSummary> getCustomerOrderDetails(String username) {

		customerValidation.validateCustomer(username);

		List<OrderSummary> orders = orderSummaryRepo.findByUsername(username);

		orders.sort(Comparator.comparing(OrderSummary::getDateOfPurchase).reversed());

		if(orders == null)
			throw new ResourceException("No Orders Found");

		return orders;
	}

	@Transactional
	public Response placeOrder(String username, OrderRequest orderRequest) {

		customerValidation.validateCustomer(username);

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

		Response resp;
		if(orderRequest.getPromoCode() != null) {
			promo = promoCodeRepo.findById(orderRequest.getPromoCode());
		}

		if(orderRequest.getPromoCode() != null && !promo.isPresent())
			throw new ResourceException("Invalid Promo Code!!!");

		if(promo.isPresent()) {
			if (!promo.get().getStatus())
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

		int beforePromo = totalPrice;
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
		OrderSummary orderSummary = new OrderSummary();

		List<OrderSummary> orderSummaryList = new ArrayList<>();

        orderSummary.setUsername(username);
        orderSummary.setProduct(findProduct.get().getProductName());
        orderSummary.setQuantity(orderRequest.getQuantity());
        orderSummary.setProductPrice(findProduct.get().getPrice());
        orderSummary.setOfferAmount(offerMoney);
        orderSummary.setFinalPrice(totalPrice);
        orderSummary.setDateOfPurchase(new Date());
        orderSummary.setOrderSummaryTransactionId(orderSummary.getOrderSummaryTransactionId());
        orderSummary.setStatus(DELIVERED.name());

		orderSummaryList.add(orderSummary);

		order.setUsername(username);
		order.setBeforePromoAmount(beforePromo);
		order.setFinalAmount(totalPrice);
		if(promo.isPresent())
			order.setPromoCode(promo.get().getCode());
		order.setTransactionId(order.getTransactionId());
		order.setDateOfPurchase(new Date());
		order.setOrderSummaries(orderSummaryList);

		orderSummary.setOrder(order);

		String offerApplied = null;

		if (findProduct.get().getOffer() != null)
			offerApplied = findProduct.get().getOffer().getOfferId().toUpperCase();

		if (offerApplied != null)
			orderSummary.setOffersApplied(offerApplied);

		OrderSummary savedOrderSummary = orderSummaryRepo.save(orderSummary);
		Order savedOrder = orderRepo.save(order);

		this.addToWalletTransaction(username, totalPrice,savedOrder);

		List<OrderSummary> orderSummaries = new ArrayList<>();
		orderSummaries.add(savedOrderSummary);

		PlacedOrder placedOrder = PlacedOrder.builder().transactionId(order.getTransactionId())
				.dateOfPurchase(order.getDateOfPurchase())
				.amount(order.getFinalAmount())
				.promoCode(order.getPromoCode())
				.orderList(orderSummaries)
				.build();

		return Response.buildResponse("Order Placed Successfully!!!", placedOrder);
	}

	@Transactional
	private void addToWalletTransaction(String username, int totalPrice, Order order) {

		WalletTransaction walletTransaction = new WalletTransaction();
		walletTransaction.setTransactionId(walletTransaction.getTransactionId());
		walletTransaction.setTransactionType(DEBITED.name());
		walletTransaction.setAmount(totalPrice);
		walletTransaction.setLoginId(username);
		walletTransaction.setReferenceId(order.getTransactionId());

		walletTransactionRepo.save(walletTransaction);
	}

	public Response getOrderByTransactionId(String transactionId) {

		Optional<Order> transactionInfo = orderRepo.findById(transactionId);

		if(!transactionInfo.isPresent())
			throw new ResourceException("In-Valid Transaction Id");

		return Response.buildResponse("Transaction Details",transactionInfo.get());

	}

	@Transactional
	public Response placeOrderV2(String loginId, String promoCode) throws InterruptedException {

		customerValidation.validateCustomer(loginId);

		Optional<Wallet> walletById = walletRepo.findById(loginId);

		if(!walletById.isPresent())
			throw new ResourceException("No Wallet Found For Customer Login");

		if (!walletById.get().getStatus())
			throw new ResourceException("Wallet Is Disabled!!! Please Enable And Place Order");

		if(walletById.get().getBalance() <= 0)
			throw new ResourceException("Insufficient Balance!!!");

		Optional<PromoCode> promo = Optional.empty();

		if(promoCode != null) {
			promo = promoCodeRepo.findById(promoCode.toUpperCase());
		}

		if(promoCode != null && !promo.isPresent())
			throw new ResourceException("Invalid Promo Code!!!");

		if(promo.isPresent()) {
			if (!promo.get().getStatus())
				throw new ResourceException("Promo Code Is Not Active!!!");
		}

		List<Cart> cartDetailsByLogin = cartRepo.findByLoginId(loginId);

		if(cartDetailsByLogin.isEmpty())
			throw new ResourceException("Cart Is Empty!!!");

		return this.orderProductsV2(cartDetailsByLogin, walletById, loginId, promo);
	}

	@Transactional
	private Response orderProductsV2(List<Cart> cartDetailsByLogin, Optional<Wallet> walletById,
									 String loginId, Optional<PromoCode> promo) throws InterruptedException {

		Order order = new Order();
		List<OrderSummary> ordersPlaced = new ArrayList<>();

		for(Cart product : cartDetailsByLogin){

			Optional<Product> findProduct = productRepo.findById(product.getProductId());

			int totalPrice = findProduct.get().getPrice() * product.getQuantity();

			int offerMoney = 0;
			if (findProduct.get().getOffer() != null) {

				offerMoney = ((totalPrice * findProduct.get().getOffer().getPercentage()) / 100);
				totalPrice = totalPrice - offerMoney;
			}

			if (walletById.get().getBalance() < totalPrice)
				throw new ResourceException("Insufficient Balance!!!");

			if(findProduct.get().getQuantityInStore() <= 0)
				throw new ResourceException("Out Of Stock!!!");

			if(findProduct.get().getQuantityInStore() < product.getQuantity())
				throw new ResourceException(String.format("Stock In Store Is %s, Please Update Cart",findProduct.get().getQuantityInStore()));

			findProduct.get().setQuantityInStore(findProduct.get().getQuantityInStore() - product.getQuantity());
			productRepo.save(findProduct.get());

			OrderSummary orderSummary = new OrderSummary();

			orderSummary.setUsername(loginId);
			orderSummary.setProduct(product.getProductId());
			orderSummary.setQuantity(product.getQuantity());
			orderSummary.setProductPrice(findProduct.get().getPrice());
			orderSummary.setOfferAmount(offerMoney);
			orderSummary.setFinalPrice(totalPrice);
			orderSummary.setDateOfPurchase(new Date());
			orderSummary.setOrderSummaryTransactionId(orderSummary.getOrderSummaryTransactionId());
			orderSummary.setStatus(DELIVERED.name());
			if(findProduct.get().getOffer() != null)
				orderSummary.setOffersApplied(findProduct.get().getOffer().getOfferId().toUpperCase());

			orderSummary.setOrder(order);
			ordersPlaced.add(orderSummary);
		}

		Order savedOrder = this.paymentUpdate(ordersPlaced, loginId, promo, order);

		cartRepo.deleteAll(cartDetailsByLogin);

		return Response.buildResponse("Order Placed Successfully!!!", savedOrder);
	}

	@Transactional
	private Order paymentUpdate(List<OrderSummary> ordersPlaced, String loginId,
								Optional<PromoCode> promo, Order order) {

		int beforePromoPrice = 0;

		for (OrderSummary orderSummary : ordersPlaced) {
			beforePromoPrice = beforePromoPrice + orderSummary.getFinalPrice();
		}

		if(promo.isPresent())
			if(promo.get().getCode() != null && !promo.isPresent())
				throw new ResourceException("Invalid Promo Code!!!");

		if(promo.isPresent()) {
			if (!promo.get().getStatus())
				throw new ResourceException("Promo Code Is Not Active!!!");
		}

		if(promo.isPresent())
			if(beforePromoPrice < promo.get().getAmount())
				throw new ResourceException("Total Amount Must Be Greater Than Promo Code Amount!!!");

		List<Order> orderByUsername = orderRepo.findByUsername(loginId);

		if(orderByUsername.size() > 0) {
			if(promo.isPresent())
				if (promo.get().getCode() != null && promo.get().getCode().toUpperCase().contentEquals("FIRSTBUY"))
					throw new ResourceException(String.format("'%s' Promo Code Is Valid On First Order!!!",promo.get().getCode().toUpperCase()));
		}

		int afterPromoPrice = beforePromoPrice;
		if(promo.isPresent())
			afterPromoPrice = beforePromoPrice - promo.get().getAmount();

		order.setTransactionId(order.getTransactionId());
		order.setUsername(loginId);
		order.setOrderSummaries(ordersPlaced);
		order.setBeforePromoAmount(beforePromoPrice);
		order.setFinalAmount(afterPromoPrice);
		order.setDateOfPurchase(new Date());
		if(promo.isPresent())
			order.setPromoCode(promo.get().getCode().toUpperCase());

		orderSummaryRepo.saveAll(ordersPlaced);
		Order savedOrder = orderRepo.save(order);

		Optional<Wallet> wallet = walletRepo.findById(loginId);
		wallet.get().setBalance(wallet.get().getBalance() - afterPromoPrice);

		walletRepo.save(wallet.get());

		this.addToWalletTransaction(loginId, afterPromoPrice, order);

		return savedOrder;
	}
}
