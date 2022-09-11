package com.demo.service;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.entity.order.OrderSummary;
import com.demo.entity.wallet.Wallet;
import com.demo.exception.custom.ResourceException;
import com.demo.model.Details;
import com.demo.model.fullinfo.FullInformation;
import com.demo.model.fullinfo.LoginDetails;
import com.demo.model.fullinfo.OrderDetails;
import com.demo.model.fullinfo.WalletDetails;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import com.demo.repository.RefundRepo;
import com.demo.repository.order.OrderRepo;
import com.demo.repository.order.OrderSummaryRepo;
import com.demo.repository.wallet.WalletRepo;
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
    OrderRepo orderRepo;

    @Autowired
    RefundRepo refundRepo;

    @Autowired
    OrderSummaryRepo orderSummaryRepo;

    public FullInformation getCustomerFullInformation(String customerId) {

        Optional<Customer> customerInfo = customerRepo.findById(customerId);
        Optional<CustomerLogin> customerLoginInfo = customerLoginRepo.findById(customerId);
        Optional<Wallet> walletInfo = walletRepo.findById(customerId);
        List<OrderSummary> orderInfo = orderSummaryRepo.findByUsername(customerId);

        FullInformation fullInformation = new FullInformation();

        if(!customerInfo.isPresent() && !customerLoginInfo.isPresent())
            throw new ResourceException("Customer Details Not Found!!!");

        fullInformation.setCustomerId(customerInfo.get().getId());
        fullInformation.setFirstName(customerInfo.get().getFirstname());
        fullInformation.setLastName(customerInfo.get().getLastname());
        fullInformation.setAge(customerInfo.get().getAge());
        fullInformation.setGender(customerInfo.get().getGender());
        fullInformation.setEmailAddress(customerInfo.get().getEmailId());

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

        orderDetails.setTotalOrders(orderInfo.size());

        List<Details> listOfOrderDetails = new LinkedList<>();

        if(orderInfo.size() != 0){

            orderInfo.forEach(order ->{

                Details oDetails = new Details();

                oDetails.setProductName(order.getProduct());
                oDetails.setQuantity(order.getQuantity());
                oDetails.setTotalAmount(order.getFinalPrice());
                oDetails.setDateOfPurchase(order.getDateOfPurchase());
                oDetails.setStatus(order.getStatus());
                if(order.getRefund() != null)
                    oDetails.setDateOfRefund(order.getRefund().getDate());

                listOfOrderDetails.add(oDetails);
            });

            Collections.reverse(listOfOrderDetails);

            orderDetails.setDetails(listOfOrderDetails);

            fullInformation.setOrderDetails(orderDetails);
        }
        else
            fullInformation.setOrderDetails(null);

        return fullInformation;
    }
}
