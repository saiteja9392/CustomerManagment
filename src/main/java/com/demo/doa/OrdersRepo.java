package com.demo.doa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.model.OrderDetails;

public interface OrdersRepo extends JpaRepository<OrderDetails, Integer>{

	@Query(value = "select * from orders where username = ?1", nativeQuery = true)
	List<OrderDetails> getOrders(String username);
}
