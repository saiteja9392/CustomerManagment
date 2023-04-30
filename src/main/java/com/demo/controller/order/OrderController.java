package com.demo.controller.order;

import com.demo.controller.RefundController;
import com.demo.controller.wallet.WalletController;
import com.demo.entity.order.Order;
import com.demo.entity.order.OrderSummary;
import com.demo.model.order.OrderRequest;
import com.demo.model.order.PlacedOrder;
import com.demo.property.CustomerProperty;
import com.demo.response.Response;
import com.demo.service.CustomerService;
import com.demo.service.order.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.demo.enumaration.Status.DELIVERED;
import static com.demo.enumaration.Status.REFUNDED;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Log4j2
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	CustomerService customerService;

	@Autowired
	CustomerProperty property;

	@Value("${spring.application.name}")
	String applicationName;

	@GetMapping("/GetOrders/{id}")
	public List<CollectionModel> getCustomerOrderDetails(@PathVariable("id") String username) {

		log.debug(customerService);

		List<OrderSummary> orderDetails = orderService.getCustomerOrderDetails(username);

		List<CollectionModel> models = new ArrayList<>();

		List<OrderSummary> deliveredOrders = orderDetails.stream().filter( order -> order.getStatus().contentEquals(DELIVERED.name())).collect(Collectors.toList());
		List<OrderSummary> refundedOrders = orderDetails.stream().filter( order -> order.getStatus().contentEquals(REFUNDED.name())).collect(Collectors.toList());

		deliveredOrders.forEach(order -> {

			CollectionModel<OrderSummary> model = CollectionModel.of(Collections.singleton(order));
			WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(methodOn(RefundController.class).initiateRefund(order.getOrderSummaryTransactionId()));
			model.add(linkTo.withRel("initiate-refund"));

			models.add(model);
		});

		refundedOrders.forEach(order -> {

			CollectionModel<OrderSummary> model = CollectionModel.of(Collections.singleton(order));

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

			PlacedOrder orderDetails = (PlacedOrder) orderStatus.getEntity();

			WebMvcLinkBuilder linkToInitiateRefund = linkTo(methodOn(RefundController.class).initiateRefund(orderDetails.getOrderList().get(0).getOrderSummaryTransactionId()));
			model.add(linkToInitiateRefund.withRel("initiate-refund"));

			WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getCustomerOrderDetails(username));
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
	public ResponseEntity<Response> getOrderByTransactionId(@PathVariable String transactionId) {

		return new ResponseEntity<>(orderService.getOrderByTransactionId(transactionId), HttpStatus.OK);
	}

	@PostMapping("/PlaceOrder/V2")
	public ResponseEntity<Response> placeOrderUpdated(@RequestParam String loginId, @RequestParam(required = false) String promoCode) throws InterruptedException {

		return new ResponseEntity<>(orderService.placeOrderV2(loginId, promoCode),HttpStatus.CREATED);
	}

	@GetMapping("/GetCustomProperty")
	public void getCustomProperty() {

		log.info(applicationName);
		log.info(property.getUsername());
		log.debug(property.getPassword());
	}

	@GetMapping("/GetOrders/V2")
	public ResponseEntity<Page<Order>> getOrdersPaginationAndSorting(@RequestParam Integer offSet, @RequestParam Integer pageSize,
													 @RequestParam(required = false) String byField){

		return new ResponseEntity<>(orderService.getOrdersPaginationAndSorting(offSet,pageSize,byField), HttpStatus.OK);
	}
}
