package com.demo.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@Table(name = "customer")
@EntityListeners(AuditingEntityListener.class)
public class Customer {

	@Id
	@Column(nullable = false, updatable = false)
	private String id;
	
	@NotNull
	@Size(min = 1,message = "FirstName should have atleast 1 Characters")
	private String firstname;
	
	@NotNull
	@Size(min = 1,message = "LastName should have atleast 1 Characters")
	private String lastname;

	@NotNull
	private int age;

	@NotNull
	private String gender;

	@CreationTimestamp
	@Column(name = "createddate",nullable = false, updatable = false)
	private Date createdDate;


	@UpdateTimestamp
	@Column(name = "modifieddate")
	private Date modifiedDate;

	@CreatedBy
	@Column(name = "createdby", nullable = false, updatable = false)
	private String createdBy;
}
