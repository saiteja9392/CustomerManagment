package com.demo.repository;

import com.demo.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepo extends JpaRepository<WalletTransaction,String> {

    List<WalletTransaction> findByLoginId(String loginId);
}
