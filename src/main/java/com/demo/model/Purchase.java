package com.demo.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;


@Entity
@Table(name = "purchase")
@JsonFilter("PurchaseFilter")
public class Purchase {

	@Id
	private String transactionid;
	private String productname;
	private int price;
	
	
	public String getTransactionId() {
		return transactionid;
	}
	public void setTransactionId(String transactionId) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		transactionId = dtf.format(now);
		
		this.transactionid = transactionId;
	}
	public String getProductName() {
		return productname;
	}
	public void setProductName(String productName) {
		this.productname = productName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
