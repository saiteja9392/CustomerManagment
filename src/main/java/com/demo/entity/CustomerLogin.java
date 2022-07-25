package com.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customerlogin")
public class CustomerLogin {

	@Id
	@NotNull
	private String loginid;
	private String password;
	private String lastlogin;
	
	private boolean admin;

	@CreationTimestamp
	@Column(name = "createddate",nullable = false, updatable = false)
	private Date createdDate;
}
