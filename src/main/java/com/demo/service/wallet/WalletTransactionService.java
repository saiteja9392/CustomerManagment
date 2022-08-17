package com.demo.service.wallet;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.entity.wallet.WalletTransaction;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import com.demo.repository.wallet.WalletTransactionRepo;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.demo.enumaration.Status.CREDITED;
import static com.demo.enumaration.Status.DEBITED;

@Service
public class WalletTransactionService {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    @Autowired
    WalletTransactionRepo walletTransactionRepo;

    public Response getWalletTransaction(String transactionId) {

        Optional<WalletTransaction> transaction = walletTransactionRepo.findById(transactionId);

        if(!transaction.isPresent())
            throw new ResourceException("No Transaction Found");

        return Response.buildResponse("Wallet Transaction Details",transaction.get());
    }

    public Response getCustomerWalletTransactions(String loginId) {

        Optional<Customer> customerInfo = customerRepo.findById(loginId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(loginId);

        if(!customerInfo.isPresent() && !customerLoginInfo.isPresent())
            throw new ResourceException("Customer Details Not Found!!!");

        List<WalletTransaction> transactionsByLoginId = walletTransactionRepo.findByLoginId(loginId);

        return Response.buildResponse("Wallet Transactions",transactionsByLoginId);

    }

    public Response getCustomerWalletTransactionsByType(String loginId, Boolean type) {

        Optional<Customer> customerInfo = customerRepo.findById(loginId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(loginId);

        Response response = null;

        if(!customerInfo.isPresent() && !customerLoginInfo.isPresent())
            throw new ResourceException("Customer Details Not Found!!!");

        List<WalletTransaction> walletInfo = walletTransactionRepo.findByLoginId(loginId);

        if(type){

            List<WalletTransaction> creditedInfo = walletInfo.stream().filter(walletTransaction -> walletTransaction.getTransactionType().contentEquals(CREDITED.name())).collect(Collectors.toList());

            Collections.sort(creditedInfo, Comparator.comparing(WalletTransaction::getTransactionTime).reversed());

            response = Response.buildResponse("Credited Details", creditedInfo);
        }
        else {
            List<WalletTransaction> debitedInfo = walletInfo.stream().filter(walletTransaction -> walletTransaction.getTransactionType().contentEquals(DEBITED.name())).collect(Collectors.toList());

            Collections.sort(debitedInfo, Comparator.comparing(WalletTransaction::getTransactionTime).reversed());

            response = Response.buildResponse("Debited Details",debitedInfo);
        }

        return response;
    }

    public List<String> getRefundTransactions(String loginId) {

        Optional<Customer> customerInfo = customerRepo.findById(loginId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(loginId);

        if(!customerInfo.isPresent() && !customerLoginInfo.isPresent())
            throw new ResourceException("Customer Details Not Found!!!");

        List<WalletTransaction> refundTransactions = walletTransactionRepo.findRefundTransactions(loginId);

        Collections.sort(refundTransactions,Comparator.comparing(WalletTransaction::getTransactionTime).reversed());

        List<String> refundTransactionIds = refundTransactions.stream().map(t -> t.getTransactionId()).collect(Collectors.toList());

        return refundTransactionIds;

    }

    public List<String> getRechargeTransactions(String loginId) {

        Optional<Customer> customerInfo = customerRepo.findById(loginId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(loginId);

        if(!customerInfo.isPresent() && !customerLoginInfo.isPresent())
            throw new ResourceException("Customer Details Not Found!!!");

        List<WalletTransaction> rechargeTransactions = walletTransactionRepo.findRechargeTransactions(loginId);

        Collections.sort(rechargeTransactions,Comparator.comparing(WalletTransaction::getTransactionTime).reversed());

        List<String> rechargeTransactionIds = rechargeTransactions.stream().map(t -> t.getTransactionId()).collect(Collectors.toList());

        return rechargeTransactionIds;
    }
}
