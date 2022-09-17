package com.demo.service;

import com.demo.entity.Refund;
import com.demo.entity.order.OrderSummary;
import com.demo.entity.wallet.Wallet;
import com.demo.entity.wallet.WalletTransaction;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.RefundRepo;
import com.demo.repository.order.OrderSummaryRepo;
import com.demo.repository.wallet.WalletRepo;
import com.demo.repository.wallet.WalletTransactionRepo;
import com.demo.response.Response;
import com.demo.validation.CustomerValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.demo.enumaration.Status.CREDITED;
import static com.demo.enumaration.Status.REFUNDED;

@Service
public class RefundService {

    @Autowired
    CustomerValidation customerValidation;

    @Autowired
    RefundRepo refundRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    WalletTransactionRepo walletTransactionRepo;

    @Autowired
    OrderSummaryRepo orderSummaryRepo;

    @Transactional
    public Response initiateRefund(String transactionId) {

        Optional<OrderSummary> findTransaction = orderSummaryRepo.findByOrderSummaryTransactionId(transactionId);

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
        orderSummaryRepo.save(findTransaction.get());

        this.addToWalletTransaction(findTransaction, refundAmount, savedRefund);

        return Response.buildResponse("Refund Successful",savedRefund);
    }

    @Transactional
    private void addToWalletTransaction(Optional<OrderSummary> findTransaction, int refundAmount, Refund refund) {

        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setTransactionId(walletTransaction.getTransactionId());
        walletTransaction.setTransactionType(CREDITED.name());
        walletTransaction.setAmount(refundAmount);
        walletTransaction.setLoginId(findTransaction.get().getUsername());
        walletTransaction.setReferenceId(refund.getTransactionId());

        walletTransactionRepo.save(walletTransaction);
    }

    public List<Refund> getRefundDetailsOfCustomer(String customerId) {

        customerValidation.validateCustomer(customerId);

        List<Refund> refundDetails = refundRepo.findByLoginId(customerId);

        refundDetails.sort(Comparator.comparing(Refund::getDate).reversed());

        return refundDetails;
    }
}
