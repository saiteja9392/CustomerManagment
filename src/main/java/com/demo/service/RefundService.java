package com.demo.service;

import com.demo.entity.*;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.*;
import com.demo.response.Response;
import com.demo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RefundService {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    @Autowired
    RefundRepo refundRepo;

    @Autowired
    OrdersRepo ordersRepo;

    @Autowired
    WalletRepo walletRepo;

    private String Status;

    public Response initiateRefund(String transactionId) {

        Optional<Orders> findTransaction = ordersRepo.findById(transactionId);

        if(!findTransaction.isPresent())
            throw new ResourceException("No Transaction Found!!!");

        Optional<Wallet> findWallet = walletRepo.findById(findTransaction.get().getUsername());

        if(!findWallet.isPresent())
            throw new ResourceException("Wallet Not Found!!!");

        if(findWallet.isPresent() && !findWallet.get().getStatus())
            throw new ResourceException("Wallet Is Disabled !!! Cannot Initiate Refund");

        int refundAmount = findTransaction.get().getFinalPrice();

        ordersRepo.deleteById(findTransaction.get().getTransactionId());

        findWallet.get().setBalance(findWallet.get().getBalance() + refundAmount);

        walletRepo.save(findWallet.get());

        Refund refund = new Refund();

        refund.setTransactionId(Utils.getRefundTransactionId());
        refund.setProduct(findTransaction.get().getProduct());
        refund.setAmount(refundAmount);
        refund.setDate(new Date());
        refund.setLoginId(findTransaction.get().getUsername());

        Response response = Response.buildResponse("Refund Successful",refundRepo.save(refund));

        return response;
    }

    public List<Refund> getRefundDetailsOfCustomer(String customerId) {

        Optional<Customer> customerInfo = customerRepo.findById(customerId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(customerId);

        if(!customerInfo.isPresent() && !customerLoginInfo.isPresent())
            throw new ResourceException("Customer Details Not Found!!!");

        List<Refund> refundDetails = refundRepo.findByLoginId(customerId);

        return refundDetails;
    }
}
