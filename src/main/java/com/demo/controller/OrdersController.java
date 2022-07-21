package com.demo.controller;

import java.util.List;

import com.demo.model.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.demo.entity.OrderDetails;
import com.demo.property.CustomerProperty;
import com.demo.service.CustomerServiceImpl;
import com.demo.service.OrdersServiceImpl;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class OrdersController {

	@Autowired
	OrdersServiceImpl ordersServiceImpl;
	
	@Autowired
	CustomerServiceImpl customerServiceImpl;

	@Autowired
	CustomerProperty property;
	
	@Value("${spring.application.name}")
	String applicationName;
	
	@GetMapping("/GetOrders/{id}")
	public List<OrderDetails> getOrderDetails(@PathVariable("id") String username) {
		
		System.out.println(customerServiceImpl);
		
		return ordersServiceImpl.getOrders(username);
	}

	@PostMapping("/PlaceOrder")
	public String placeOrder(@RequestBody OrderRequest orderRequest) throws Exception {

		return ordersServiceImpl.placeOrder(orderRequest);
	}
	
	@GetMapping("/GetCustomProperty")
	public void getCustomProperty() {
		
		log.info(applicationName);
		log.info(property.getUsername());
		log.debug(property.getPassword());
	}
}
