package com.demo.service;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.entity.OrderDetails;
import com.demo.entity.Product;
import com.demo.exception.custom.ResourceException;
import com.demo.model.OrderRequest;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import com.demo.repository.OrdersRepo;
import com.demo.repository.ProductRepo;
import com.demo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public String placeOrder(String username, OrderRequest orderRequest) {

		Optional<Customer> c = cRepo.findById(username);
		Optional<CustomerLogin> cLogin = cLoginRepo.findById(username);
		Optional<Product> findProduct = productRepo.findById(orderRequest.getProductId());

		OrderDetails order = new OrderDetails();

		if(!c.isPresent()|| !cLogin.isPresent())
			throw new ResourceException("No Login Found");

		if(!findProduct.isPresent())
			throw new ResourceException(String.format("No Product Found With Product Id - %s",orderRequest.getProductId()));

		else {

			if(orderRequest.getQuantity() <= findProduct.get().getQuantityInStore()){

				findProduct.get().setQuantityInStore(findProduct.get().getQuantityInStore() - orderRequest.getQuantity());
				productRepo.save(findProduct.get());

				order.setUsername(username);
				order.setProduct(findProduct.get().getProductName());
				order.setPrice(findProduct.get().getPrice());
				order.setDateOfPurchase(Utils.getCurrentTimeStamp());
				order.setTransactionId(order.getTransactionId());

				oRepo.save(order);

				Status = "Item Placed Successfully";
			}
		}

		return Status;
	}
}
