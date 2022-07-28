package com.demo.controller;

import com.demo.entity.OrderDetails;
import com.demo.model.OrderRequest;
import com.demo.property.CustomerProperty;
import com.demo.service.CustomerService;
import com.demo.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
public class OrdersController {

	@Autowired
	OrderService orderService;
	
	@Autowired
	CustomerService customerService;

	@Autowired
	CustomerProperty property;
	
	@Value("${spring.application.name}")
	String applicationName;
	
	@GetMapping("/GetOrders/{id}")
	public List<OrderDetails> getOrderDetails(@PathVariable("id") String username) {
		
		System.out.println(customerService);
		
		return orderService.getOrders(username);
	}

	@PostMapping("/PlaceOrder")
	public String placeOrder(@RequestParam String username, @RequestBody OrderRequest orderRequest){

		return orderService.placeOrder(username,orderRequest);
	}
	
	@GetMapping("/GetCustomProperty")
	public void getCustomProperty() {
		
		log.info(applicationName);
		log.info(property.getUsername());
		log.debug(property.getPassword());
	}
}
