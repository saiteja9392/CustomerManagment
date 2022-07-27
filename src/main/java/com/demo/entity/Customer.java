package com.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
@EntityListeners(AuditingEntityListener.class)
public class Customer {

	@Id
	@Column(nullable = false)
	private String id;
	
	@NotNull
	@Size(min = 1,message = "FirstName should have at-least 1 Characters")
	private String firstname;
	
	@NotNull
	@Size(min = 1,message = "LastName should have at-least 1 Characters")
	private String lastname;

	@NotNull
	private int age;

	@NotNull
	private String gender;

	@CreatedDate
	@Temporal(TemporalType.DATE)
	@Column(name = "createddate",nullable = false, updatable = false)
	private Date createdDate;


	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifieddate")
	private Date modifiedDate;

	@CreatedBy
	@Column(name = "createdby", nullable = false, updatable = false)
	private String createdBy;
}
