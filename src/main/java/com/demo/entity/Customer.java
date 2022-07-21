package com.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@NotNull
	private String id;
	
	@NotNull
	@Size(min = 2,message = "FirstName should have atleast 2 Characters")
	private String firstname;
	
	@NotNull
	@Size(min = 2,message = "LastName should have atleast 2 Characters")
	private String lastname;
		
}
