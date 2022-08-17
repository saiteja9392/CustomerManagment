package com.demo.repository.wallet;

import com.demo.entity.wallet.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WalletTransactionRepo extends JpaRepository<WalletTransaction,String> {

    List<WalletTransaction> findByLoginId(String loginId);

    @Modifying
    @Query(value = "delete from wallettransactions where loginid = ?",nativeQuery = true)
    int deleteCustomerWalletTransactions(String deleteCustomer);

    @Query(value = "select * from wallettransactions where referenceid like 'REF%' and loginid = ?",nativeQuery = true)
    List<WalletTransaction> findRefundTransactions(String loginId);

    @Query(value = "select * from wallettransactions where referenceid like 'B%' and loginid = ?",nativeQuery = true)
    List<WalletTransaction> findRechargeTransactions(String loginId);
}
