package com.demo.service;

import com.demo.entity.*;
import com.demo.exception.custom.ResourceException;
import com.demo.model.OrderRequest;
import com.demo.repository.*;
import com.demo.response.Response;
import com.demo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

	@Autowired
	CustomerRepo cRepo;

	@Autowired
	CustomerLoginRepo cLoginRepo;

	@Autowired
	OrdersRepo oRepo;

	@Autowired
	ProductRepo productRepo;

	@Autowired
	WalletRepo walletRepo;

	public String Status = "";

	public List<Orders> getAllOrderDetails(String username) {
		
		System.out.println(oRepo.count());

		Optional<Customer> c = cRepo.findById(username);

		Optional<CustomerLogin> cLogin = cLoginRepo.findById(username);

		if(!c.isPresent()|| !cLogin.isPresent())
			throw new ResourceException("No Login Found");
		
		List<Orders> orders = oRepo.findByUsername(username);

		Collections.sort(orders, Comparator.comparing(Orders::getTransactionId).reversed());

		if(orders == null)
			throw new ResourceException("No Orders Found");
		
		return orders;
	}

	public Response placeOrder(String username, OrderRequest orderRequest) {

		Optional<Customer> c = cRepo.findById(username);
		Optional<CustomerLogin> cLogin = cLoginRepo.findById(username);
		Optional<Product> findProduct = productRepo.findById(orderRequest.getProductId());
		Optional<Wallet> walletById = walletRepo.findById(username);


		Orders order = new Orders();

		if(!c.isPresent()|| !cLogin.isPresent())
			throw new ResourceException("No Login Found");

		Response resp;

		if(!findProduct.isPresent())
			throw new ResourceException(String.format("No Product Found With Product Id - %s",orderRequest.getProductId()));

		else {

			if(!walletById.isPresent()) {
				throw new ResourceException("No Wallet Found For Customer Login");
			}

			else if (!walletById.get().getStatus()) {
				throw new ResourceException("Wallet Is Disabled!!! Please Enable And Place Order");
			}

			else{

				if(walletById.get().getBalance() <= 0)
					throw new ResourceException("Insufficient Balance!!!");

				else{

					if(findProduct.get().getQuantityInStore() == 0)
						throw new ResourceException("Out Of Stock!!!");

					else if(orderRequest.getQuantity() > findProduct.get().getQuantityInStore())
						throw new ResourceException(String.format("Available Stock In Store - %s, Please Select As Per That",
								findProduct.get().getQuantityInStore()));

					else{

						int totalPrice = findProduct.get().getPrice() * orderRequest.getQuantity();

						if(walletById.get().getBalance() < totalPrice)
							throw new ResourceException("Insufficient Balance!!!");

						Wallet w = new Wallet();

						w.setWalletId(username);
						w.setBalance(walletById.get().getBalance() - totalPrice);
						w.setStatus(walletById.get().getStatus());

						cLogin.get().setWallet(w);

						cLoginRepo.save(cLogin.get());

						findProduct.get().setQuantityInStore(findProduct.get().getQuantityInStore() - orderRequest.getQuantity());
						productRepo.save(findProduct.get());

						order.setUsername(username);
						order.setProduct(findProduct.get().getProductName());
						order.setQuantity(orderRequest.getQuantity());
						order.setTotalPrice(totalPrice);
						order.setDateOfPurchase(Utils.getCurrentTimeStamp());
						order.setTransactionId(order.getTransactionId());

						oRepo.save(order);

						resp = Response.buildResponse("Order Placed Successfully!!!");
					}
				}
			}
		}

		return resp;
	}
}
