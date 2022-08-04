package com.demo.repository;

import com.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, String>{

	List<Order> findByUsername(String username);
}
