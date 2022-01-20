package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.Purchase;
import com.demo.service.PurchaseServiceImpl;

@RestController
public class PurchaseController {

	@Autowired
	PurchaseServiceImpl purchaseServiceImpl;
	
	@PostMapping("/BuyItem/{id}")
	public String addPurchase(@RequestBody Purchase purchase, @PathVariable("id") String username) throws Exception {
		
		return purchaseServiceImpl.addPurchase(purchase,username);
	}
}
