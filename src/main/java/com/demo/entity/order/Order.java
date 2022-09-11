package com.demo.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class Order {

	@Id
	private String transactionId;
	
	@JsonIgnore
	private String username;

	@Column(name = "dateofpurchase")
	private Date dateOfPurchase;

	private Integer beforePromoAmount;

	@Column(name = "promocode")
	private String promoCode;

	@Column(name = "finalamount")
	private Integer finalAmount;

	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
	private List<OrderSummary> orderSummaries = new ArrayList<>();

	public void setTransactionId(String transactionId) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		transactionId = dtf.format(now);

		this.transactionId = transactionId;
	}
}
