package com.demo.repository.wallet;

import com.demo.entity.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, String> {
}
