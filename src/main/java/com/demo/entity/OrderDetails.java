package com.demo.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "orders")
public class OrderDetails {

	@Id
	@Column(name = "transactionid")
	private String transactionId;
	
	@JsonIgnore
	private String username;
	
	private String product;

	@Column(name = "dateofpurchase")
	private String dateOfPurchase;
	
	private int price;

	public void setTransactionId(String transactionId) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		transactionId = dtf.format(now);

		this.transactionId = transactionId;
	}
}
