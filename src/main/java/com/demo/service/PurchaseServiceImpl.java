package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.doa.CustomerLoginRepo;
import com.demo.doa.CustomerRepo;
import com.demo.doa.OrdersRepo;
import com.demo.doa.PurchaseRepo;
import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.entity.OrderDetails;
import com.demo.entity.Purchase;
import com.demo.exception.custom.ResourceException;
import com.demo.util.Utils;

@Service
public class PurchaseServiceImpl {

	@Autowired
	PurchaseRepo pRepo;
	
	@Autowired
	CustomerRepo cRepo;
	
	@Autowired
	CustomerLoginRepo cLoginRepo;
	
	@Autowired
	OrdersRepo oRepo;
	
	private String Status = "";
	
	public String addPurchase(Purchase purchase, String username) throws Exception {
		
		Customer c = cRepo.findById(username);
		CustomerLogin cLogin = cLoginRepo.findByLoginid(username);
		
		OrderDetails order = new OrderDetails();
		
		System.out.println(purchase.getProductName());
		
		if(c == null || cLogin == null) {
			throw new ResourceException("No Login Found");
		}
		
		else {
			Purchase purchaseDetails = new Purchase();

			purchaseDetails.setTransactionId(purchaseDetails.getTransactionId());
			purchaseDetails.setProductName(purchase.getProductName());
			purchaseDetails.setPrice(purchase.getPrice());

			pRepo.save(purchaseDetails);

			order.setUsername(username);
			order.setProduct(purchase.getProductName());
			order.setDateOfPurchase(Utils.getOrderDate());
			order.setPrice(purchase.getPrice());
			
			oRepo.save(order);
			
			Status = "Item Placed Successfully";
		}
		
		return Status;
	}

	public List<Purchase> getAllTransactionIDs() {
		
		List<Purchase> purchase = pRepo.findAll();
		
		return purchase;
	}

	
}
