package com.demo.repository.order;

import com.demo.entity.order.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderSummaryRepo extends JpaRepository<OrderSummary, String> {
    Optional<OrderSummary> findByOrderSummaryTransactionId(String transactionId);

    List<OrderSummary> findByUsername(String username);
}
