package com.demo.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sno;
	
	@JsonIgnore
	private String username;
	
	private String product;

	@Column(name = "dateofpurchase")
	private String dateOfPurchase;
	
	private int price;
}
