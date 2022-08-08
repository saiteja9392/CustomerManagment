package com.demo.repository.wallet;

import com.demo.entity.wallet.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepo extends JpaRepository<WalletTransaction,String> {

    List<WalletTransaction> findByLoginId(String loginId);
}
