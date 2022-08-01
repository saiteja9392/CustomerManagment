package com.demo.repository;

import com.demo.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefundRepo extends JpaRepository<Refund,String> {
    List<Refund> findByLoginId(String customerId);
}
