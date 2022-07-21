package com.demo.service;

import java.util.List;
import java.util.Optional;

import com.demo.doa.CustomerLoginRepo;
import com.demo.doa.CustomerRepo;
import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.model.OrderRequest;
import com.demo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.doa.OrdersRepo;
import com.demo.entity.OrderDetails;
import com.demo.exception.custom.ResourceException;

@Service
public class OrdersServiceImpl {

	@Autowired
	CustomerRepo cRepo;

	@Autowired
	CustomerLoginRepo cLoginRepo;

	@Autowired
	OrdersRepo oRepo;

	public String Status = "";

	public List<OrderDetails> getOrders(String username) {
		
		System.out.println(oRepo.count());

		Optional<Customer> c = cRepo.findById(username);

		Optional<CustomerLogin> cLogin = cLoginRepo.findById(username);

		if(!c.isPresent()|| !cLogin.isPresent())
			throw new ResourceException("No Login Found");
		
		List<OrderDetails> orders = oRepo.findByUsername(username);

		if(orders == null)
			throw new ResourceException("No Orders Found");
		
		return orders;
	}

	public String placeOrder(OrderRequest orderRequest) {

		Optional<Customer> c = cRepo.findById(orderRequest.getUsername());
		Optional<CustomerLogin> cLogin = cLoginRepo.findById(orderRequest.getUsername());

		OrderDetails order = new OrderDetails();

		System.out.println(orderRequest.getProductName());

		if(!c.isPresent()|| !cLogin.isPresent())
			throw new ResourceException("No Login Found");


		else {

			order.setUsername(orderRequest.getUsername());
			order.setProduct(orderRequest.getProductName());
			order.setPrice(orderRequest.getPrice());
			order.setDateOfPurchase(Utils.getCurrentTimeStamp());
			order.setTransactionId(order.getTransactionId());

			oRepo.save(order);

			Status = "Item Placed Successfully";
		}

		return Status;
	}
}
