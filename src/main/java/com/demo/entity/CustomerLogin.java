package com.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	
	private String isAdmin;
}
