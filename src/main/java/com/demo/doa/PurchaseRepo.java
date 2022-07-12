package com.demo.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.Purchase;

public interface PurchaseRepo extends JpaRepository<Purchase, String>{

	
}
