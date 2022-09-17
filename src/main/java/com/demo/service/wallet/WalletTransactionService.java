package com.demo.service.wallet;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.entity.wallet.WalletTransaction;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import com.demo.repository.wallet.WalletTransactionRepo;
import com.demo.response.Response;
import com.demo.validation.CustomerValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.demo.enumaration.Status.CREDITED;
import static com.demo.enumaration.Status.DEBITED;

@Service
public class WalletTransactionService {

    @Autowired
    CustomerValidation customerValidation;

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

        if(!customerInfo.isPresent() && !customerLoginInfo.isPresent())
            throw new ResourceException("Customer Details Not Found!!!");

        List<WalletTransaction> walletInfo = walletTransactionRepo.findByLoginId(loginId);

        Response response;
        if(type){

            List<WalletTransaction> creditedInfo = walletInfo.stream()
                    .filter(walletTransaction -> walletTransaction.getTransactionType().contentEquals(CREDITED.name()))
                    .sorted(Comparator.comparing(WalletTransaction::getTransactionTime).reversed()).collect(Collectors.toList());

            response = Response.buildResponse("Credited Details", creditedInfo);
        }
        else {
            List<WalletTransaction> debitedInfo = walletInfo.stream()
                    .filter(walletTransaction -> walletTransaction.getTransactionType().contentEquals(DEBITED.name()))
                    .sorted(Comparator.comparing(WalletTransaction::getTransactionTime).reversed()).collect(Collectors.toList());

            response = Response.buildResponse("Debited Details",debitedInfo);
        }

        return response;
    }

    public List<String> getRefundTransactions(String loginId) {

        customerValidation.validateCustomer(loginId);

        List<WalletTransaction> refundTransactions = walletTransactionRepo.findRefundTransactions(loginId);

        refundTransactions.sort(Comparator.comparing(WalletTransaction::getTransactionTime).reversed());

        return refundTransactions.stream().map(WalletTransaction::getTransactionId).collect(Collectors.toList());

    }

    public List<String> getRechargeTransactions(String loginId) {

        customerValidation.validateCustomer(loginId);

        List<WalletTransaction> rechargeTransactions = walletTransactionRepo.findRechargeTransactions(loginId);

        rechargeTransactions.sort(Comparator.comparing(WalletTransaction::getTransactionTime).reversed());

        return rechargeTransactions.stream().map(WalletTransaction::getTransactionId).collect(Collectors.toList());
    }
}
