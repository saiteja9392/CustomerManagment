package com.demo.service;

import com.demo.entity.*;
import com.demo.exception.custom.ResourceException;
import com.demo.model.*;
import com.demo.repository.*;
import com.demo.util.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerFullInformationService {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    OrdersRepo ordersRepo;

    @Autowired
    RefundRepo refundRepo;

    public FullInformation getCustomerFullInformation(String customerId) {

        Optional<Customer> customerInfo = customerRepo.findById(customerId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(customerId);
        Optional<Wallet> walletInfo = walletRepo.findById(customerId);
        List<Orders> ordersInfo = ordersRepo.findByUsername(customerId);

        FullInformation fullInformation = new FullInformation();

        if(!customerInfo.isPresent() && !customerLoginInfo.isPresent())
            throw new ResourceException("Customer Details Not Found!!!");

        fullInformation.setCustomerId(customerInfo.get().getId());
        fullInformation.setFirstName(customerInfo.get().getFirstname());
        fullInformation.setLastName(customerInfo.get().getLastname());
        fullInformation.setAge(customerInfo.get().getAge());
        fullInformation.setGender(customerInfo.get().getGender());

        LoginDetails loginDetails = new LoginDetails();

        loginDetails.setLoginId(customerLoginInfo.get().getLoginid());
        loginDetails.setPassword(AES.decrypt(customerLoginInfo.get().getPassword()));

        fullInformation.setLoginDetails(loginDetails);

        if(walletInfo.isPresent()){

            WalletDetails walletDetails = new WalletDetails();

            walletDetails.setWalletId(walletInfo.get().getWalletId());
            walletDetails.setBalance(walletInfo.get().getBalance());

            if(walletInfo.get().getStatus())
                walletDetails.setStatus("Enabled");
            else
                walletDetails.setStatus("Disabled");

            fullInformation.setWalletDetails(walletDetails);
        }
        else
            fullInformation.setWalletDetails(null);

        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setTotalOrders(ordersInfo.size());

        List<Details> listOfOrderDetails = new LinkedList<>();

        if(ordersInfo.size() != 0){

            ordersInfo.forEach(order ->{

                Details oDetails = new Details();

                oDetails.setProductName(order.getProduct());
                oDetails.setQuantity(order.getQuantity());
                oDetails.setTotalAmount(order.getFinalPrice());
                oDetails.setDateOfPurchase(order.getDateOfPurchase());

                listOfOrderDetails.add(oDetails);
            });

            Collections.reverse(listOfOrderDetails);

            orderDetails.setDetails(listOfOrderDetails);

            fullInformation.setOrderDetails(orderDetails);
        }
        else
            fullInformation.setOrderDetails(null);

        List<Refund> refundDetails = refundRepo.findByLoginId(customerId);

        List<RefundDetails> listOfRefundDetails = new LinkedList<>();

        if(refundDetails.size() != 0){

            refundDetails.forEach(r -> {

                RefundDetails refund = new RefundDetails();

                refund.setProductName(r.getProduct());
                refund.setAmount(r.getAmount());
                refund.setDate(r.getDate());

                listOfRefundDetails.add(refund);

            });

            Collections.reverse(listOfRefundDetails);

            fullInformation.setRefundDetails(listOfRefundDetails);
        }
        else
            fullInformation.setRefundDetails(null);

        return fullInformation;
    }
}
