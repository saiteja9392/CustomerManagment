package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.doa.OrdersRepo;
import com.demo.model.OrderDetails;

@Service
public class OrdersServiceImpl {

	@Autowired
	OrdersRepo oRepo;

	public List<OrderDetails> getOrders(String username) {
		
		List<OrderDetails> orders = oRepo.getOrders(username);
		
		return orders;
	}
}
