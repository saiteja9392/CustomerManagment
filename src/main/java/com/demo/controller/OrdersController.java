package com.demo.controller;

import com.demo.entity.Orders;
import com.demo.model.OrderRequest;
import com.demo.property.CustomerProperty;
import com.demo.response.Response;
import com.demo.service.CustomerService;
import com.demo.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
	public List<CollectionModel> getAllOrderDetails(@PathVariable("id") String username) {
		
		log.debug(customerService);

		List<Orders> orderDetails = orderService.getAllOrderDetails(username);

		List<CollectionModel> models = new ArrayList<>();

		orderDetails.forEach(order -> {

			CollectionModel<Orders> model = CollectionModel.of(Collections.singleton(order));
			WebMvcLinkBuilder linkTo = linkTo(methodOn(RefundController.class).initiateRefund(order.getTransactionId()));
			model.add(linkTo.withRel("initiate-refund"));

			models.add(model);
		});

		return models;
	}

	@PostMapping("/PlaceOrder")
	public EntityModel<Response> placeOrder(@RequestParam String username, @Valid @RequestBody OrderRequest orderRequest) {

		EntityModel<Response> model = null;
		Response orderStatus = null;

		try {
			orderStatus = orderService.placeOrder(username, orderRequest);

			model = EntityModel.of(orderStatus);

			Orders orderDetails = (Orders) orderStatus.getEntity();

			WebMvcLinkBuilder linkToInitiateRefund = linkTo(methodOn(RefundController.class).initiateRefund(orderDetails.getTransactionId()));
			model.add(linkToInitiateRefund.withRel("initiate-refund"));

			WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllOrderDetails(username));
			model.add(linkTo.withRel("all-orders"));

		} catch (Exception e) {

			if(e.getMessage().contentEquals("Insufficient Balance!!!")){

				model = EntityModel.of(Response.builder().message(e.getMessage()).date(new Date()).build());

				WebMvcLinkBuilder linkToInitiateRefund = linkTo(methodOn(WalletController.class).addMoneyToWallet(username,100));
				model.add(linkToInitiateRefund.withRel("add-money"));
			}
			else{
				throw e;
			}
		}

		return model;
	}

	@GetMapping("/GetOrderByTransactionId/{transactionId}")
	public EntityModel<Orders> getOrderByTransactionId(@PathVariable String transactionId){

		Orders order = orderService.getOrderByTransactionId(transactionId).get();

		EntityModel<Orders> model = EntityModel.of(order);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(RefundController.class).initiateRefund(order.getTransactionId()));
		model.add(linkTo.withRel("initiate-refund"));

		return model;
	}
	
	@GetMapping("/GetCustomProperty")
	public void getCustomProperty() {
		
		log.info(applicationName);
		log.info(property.getUsername());
		log.debug(property.getPassword());
	}
}
