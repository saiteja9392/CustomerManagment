package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.doa.OrdersRepo;
import com.demo.entity.OrderDetails;
import com.demo.exception.custom.ResourceException;

@Service
public class OrdersServiceImpl {

	@Autowired
	OrdersRepo oRepo;

	public List<OrderDetails> getOrders(String username) {
		
		System.out.println(oRepo.count());
		
		List<OrderDetails> orders = oRepo.findByUsername(username);
		
		if(orders == null)
			throw new ResourceException("No Orders Found");
		
		return orders;
	}
}
