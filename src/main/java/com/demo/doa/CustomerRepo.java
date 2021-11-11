package com.demo.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Integer>{
	
	Customer findById(String id);
	
}
