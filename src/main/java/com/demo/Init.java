package com.demo;

import com.demo.doa.CustomerLoginRepo;
import com.demo.doa.CustomerRepo;
import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.util.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Optional;

@Component
public class Init {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    private static final Customer c;

    private static final CustomerLogin cl;

    static {

        c = Customer.builder()
                .id("admin")
                .firstname("admin")
                .lastname("admin")
                .age(0)
                .gender("O")
                .createdDate(new Date())
                .modifiedDate(null)
                .createdBy("system")
                .build();

        cl = CustomerLogin.builder()
                .loginid("admin")
                .password(AES.encrypt("admin"))
                .admin(true)
                .createdDate(new Date())
                .lastlogin(null)
                .version(0L)
                .build();
    }

    @PostConstruct
    public void addAdminUserWhenStart(){

        Optional<Customer> customer = customerRepo.findById(c.getId());
        Optional<CustomerLogin> customerLogin = customerLoginRepo.findById(cl.getLoginid());

        if(!customer.isPresent()) {
            customerRepo.save(c);

            if(!customerLogin.isPresent())
                customerLoginRepo.save(cl);
        }
        if(customer.isPresent() && !customerLogin.isPresent())
            customerLoginRepo.save(cl);

    }
}
