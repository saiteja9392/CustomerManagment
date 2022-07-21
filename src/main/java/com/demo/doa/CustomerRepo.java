package com.demo.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, String>{
	
}
