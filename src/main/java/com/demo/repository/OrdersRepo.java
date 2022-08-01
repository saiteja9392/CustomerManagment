package com.demo.repository;

import com.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepo extends JpaRepository<Orders, String>{

	List<Orders> findByUsername(String username);
}
