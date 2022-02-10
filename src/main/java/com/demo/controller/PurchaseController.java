package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.Purchase;
import com.demo.service.PurchaseServiceImpl;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class PurchaseController {

	@Autowired
	PurchaseServiceImpl purchaseServiceImpl;
	
	@PostMapping("/BuyItem/{id}")
	public String addPurchase(@RequestBody Purchase purchase, @PathVariable("id") String username) throws Exception {
		
		return purchaseServiceImpl.addPurchase(purchase,username);
	}
	
	@GetMapping("/GetAllTransactions")
	public MappingJacksonValue getAllTransactionIDs() {
		
		List<Purchase> purchases = purchaseServiceImpl.getAllTransactionIDs();
		
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("transactionid");
		
		FilterProvider filters = new SimpleFilterProvider().addFilter("PurchaseFilter", filter);
		
		MappingJacksonValue mapping = new MappingJacksonValue(purchases);
		
		mapping.setFilters(filters);
		
		return mapping;
	}
}
