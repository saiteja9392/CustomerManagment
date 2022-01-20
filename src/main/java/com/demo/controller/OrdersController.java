package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.OrderDetails;
import com.demo.service.OrdersServiceImpl;

@RestController
public class OrdersController {

	@Autowired
	OrdersServiceImpl ordersServiceImpl;
	
	@GetMapping("/GetOrders/{id}")
	public List<OrderDetails> getOrderDetails(@PathVariable("id") String username) {
		
		return ordersServiceImpl.getOrders(username);
	}
}
