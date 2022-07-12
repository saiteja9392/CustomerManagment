package com.demo.doa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.entity.OrderDetails;

public interface OrdersRepo extends JpaRepository<OrderDetails, Integer>{

	List<OrderDetails> findByUsername(String username);
}
