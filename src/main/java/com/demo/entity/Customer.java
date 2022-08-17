package com.demo.entity;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
	@Range(min = 1,max = 100,message = "Age Cannot Be Zero or Negative")
	private int age;

	@NotNull
	private String gender;

	@Column(name = "emailid", unique = true, nullable = false)
	@Email(regexp = "[a-z0-9._]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Invalid Email")
	private String emailId;

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
