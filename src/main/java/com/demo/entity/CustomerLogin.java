package com.demo.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "customerlogin")
public class CustomerLogin {

	@Id
	@NotNull
	private String loginid;
	private String password;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastlogin;
	
	private boolean admin;

	@CreationTimestamp
	@Column(name = "createddate",nullable = false, updatable = false)
	private Date createdDate;

	@Version
	private Long version;
}
