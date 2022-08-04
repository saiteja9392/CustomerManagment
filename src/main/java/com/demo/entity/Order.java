package com.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@Column(name = "transactionid")
	private String transactionId;
	
	@JsonIgnore
	private String username;
	
	private String product;

	@Column(name = "dateofpurchase")
	private Date dateOfPurchase;

	@NotNull
	private Integer quantity;

	@Column(name = "productprice",nullable = false)
	private Integer productPrice;

	@Column(name = "offeramount")
	private Integer offerAmount;

	@Column(name = "promoamount")
	private Integer promoAmount;

	@Column(name = "offersapplied")
	private String offersApplied;

	@Column(name = "finalprice",nullable = false)
	private int finalPrice;

	private String status;

	@OneToOne
	@JoinColumn(name = "refund_transactionid")
	private Refund refund;

	public void setTransactionId(String transactionId) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		transactionId = dtf.format(now);

		this.transactionId = transactionId;
	}
}
