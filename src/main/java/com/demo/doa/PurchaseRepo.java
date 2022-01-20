package com.demo.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Purchase;

public interface PurchaseRepo extends JpaRepository<Purchase, String>{

	
}
