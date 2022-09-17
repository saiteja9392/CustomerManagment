package com.demo.validation;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerValidation {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    public void validateCustomer(String loginId){

        Optional<Customer> customer = customerRepo.findById(loginId);

        if(!customer.isPresent())
            throw new ResourceException("Customer Details Not Found!!!");

        Optional<CustomerLogin> customerLogin = customerLoginRepo.findById(loginId);

        if(!customerLogin.isPresent())
            throw new ResourceException("Customer Login Details Not Found!!!");
    }
}
