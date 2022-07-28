package com.demo.repository;

import com.demo.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepo extends JpaRepository<OrderDetails, Integer>{

	List<OrderDetails> findByUsername(String username);
}
