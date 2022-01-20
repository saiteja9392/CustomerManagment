package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.doa.CustomerLoginRepo;
import com.demo.doa.CustomerRepo;
import com.demo.doa.OrdersRepo;
import com.demo.doa.PurchaseRepo;
import com.demo.model.Customer;
import com.demo.model.CustomerLogin;
import com.demo.model.OrderDetails;
import com.demo.model.Purchase;
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
		
		if(c == null || cLogin == null) {
			Status = "No Login Found";
		}
		
		else {
			Purchase purchaseDetails = new Purchase();

			purchaseDetails.setTransactionId(purchase.getTransactionId());
			purchaseDetails.setProductName(purchase.getProductName());
			purchaseDetails.setPrice(purchase.getPrice());

			pRepo.save(purchaseDetails);

			order.setUsername(username);
			order.setProduct(purchase.getProductName());
			order.setDateofPurchase(Utils.getOrderDate());
			order.setPrice(purchase.getPrice());
			
			oRepo.save(order);
			
			Status = "Item Placed Successfully";
		}
		
		return Status;
	}

	
}
