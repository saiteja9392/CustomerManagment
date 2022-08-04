package com.demo.service;

import com.demo.entity.*;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.*;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.demo.enumaration.Status.CREDITED;
import static com.demo.enumaration.Status.REFUNDED;

@Service
public class RefundService {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    @Autowired
    RefundRepo refundRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    WalletTransactionRepo walletTransactionRepo;

    @Transactional
    public Response initiateRefund(String transactionId) {

        Optional<Order> findTransaction = orderRepo.findById(transactionId);

        if(!findTransaction.isPresent())
            throw new ResourceException("No Transaction Found!!!");

        Optional<Wallet> findWallet = walletRepo.findById(findTransaction.get().getUsername());

        if(!findWallet.isPresent())
            throw new ResourceException("Wallet Not Found!!!");

        if(findWallet.isPresent() && !findWallet.get().getStatus())
            throw new ResourceException("Wallet Is Disabled !!! Cannot Initiate Refund");

        int refundAmount = findTransaction.get().getFinalPrice();

        findWallet.get().setBalance(findWallet.get().getBalance() + refundAmount);

        walletRepo.save(findWallet.get());

        Refund refund = new Refund();

        refund.setTransactionId(refund.getTransactionId());
        refund.setProduct(findTransaction.get().getProduct());
        refund.setAmount(refundAmount);
        refund.setDate(new Date());
        refund.setLoginId(findTransaction.get().getUsername());

        Refund savedRefund = refundRepo.save(refund);

        findTransaction.get().setStatus(REFUNDED.name());
        findTransaction.get().setRefund(savedRefund);
        orderRepo.save(findTransaction.get());

        addToWalletTransaction(findTransaction, refundAmount, savedRefund);

        Response response = Response.buildResponse("Refund Successful",savedRefund);

        return response;
    }

    @Transactional
    private WalletTransaction addToWalletTransaction(Optional<Order> findTransaction, int refundAmount, Refund refund) {

        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setTransactionId(walletTransaction.getTransactionId());
        walletTransaction.setTransactionType(CREDITED.name());
        walletTransaction.setAmount(refundAmount);
        walletTransaction.setLoginId(findTransaction.get().getUsername());
        walletTransaction.setReferenceId(refund.getTransactionId());

        return walletTransactionRepo.save(walletTransaction);
    }

    public List<Refund> getRefundDetailsOfCustomer(String customerId) {

        Optional<Customer> customerInfo = customerRepo.findById(customerId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(customerId);

        if(!customerInfo.isPresent() && !customerLoginInfo.isPresent())
            throw new ResourceException("Customer Details Not Found!!!");

        List<Refund> refundDetails = refundRepo.findByLoginId(customerId);

        Collections.sort(refundDetails, Comparator.comparing(Refund::getDate).reversed());

        return refundDetails;
    }
}
