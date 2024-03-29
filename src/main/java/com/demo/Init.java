package com.demo;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.entity.Product;
import com.demo.entity.support.Sla;
import com.demo.entity.wallet.Wallet;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import com.demo.repository.ProductRepo;
import com.demo.repository.support.SlaRepo;
import com.demo.repository.wallet.WalletRepo;
import com.demo.util.AES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class Init {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CustomerLoginRepo customerLoginRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    SlaRepo slaRepo;

    private static final Customer c;

    private static CustomerLogin cl;

    private static final Product p1;

    private static final Product p2;

    private static Wallet w;

    private static List<Sla> s;

    static {

        log.debug("Inside static block");

        c = Customer.builder()
                .id("admin")
                .firstname("admin")
                .lastname("admin")
                .age(100)
                .gender("O")
                .emailId("admin@gmail.com")
                .createdDate(new Date())
                .modifiedDate(null)
                .createdBy("system")
                .build();

        w = Wallet.builder()
                .walletId("admin")
                .balance(10000)
                .status(true)
                .build();

        cl = CustomerLogin.builder()
                .loginid("admin")
                .password(AES.encrypt("admin"))
                .admin(true)
                .createdDate(new Date())
                .lastlogin(null)
                .wallet(w)
                .build();

        p1 = Product.builder()
                .productId("CM001")
                .productName("Note Books")
                .price(25)
                .manufacturer("ClassMate")
                .quantityInStore(10)
                .createdBy("system")
                .build();

        p2 = Product.builder()
                .productId("CM002")
                .productName("Text Books")
                .price(50)
                .manufacturer("Oxford")
                .quantityInStore(0)
                .createdBy("system")
                .build();

        s = Arrays.asList(new Sla("P1",4L),new Sla("P2",3L),new Sla("P3",2L),new Sla("P4",1L));
    }

    @PostConstruct
    public void addAdminUserWhenStart(){

        log.debug("Inside addAdminUserWhenStart");

        Optional<Wallet> walletAdmin = walletRepo.findById("admin");

        if(!walletAdmin.isPresent())
            walletRepo.save(w);

        Optional<Customer> customer = customerRepo.findById(c.getId());
        Optional<CustomerLogin> customerLogin = customerLoginRepo.findById(cl.getLoginid());

        if(!customer.isPresent()) {
            log.debug("Inside !customer.isPresent()");

            customerRepo.save(c);

            if(!customerLogin.isPresent()) {
                log.debug("Inside !customerLogin.isPresent()");

                customerLoginRepo.save(cl);
            }
        }

        if(customer.isPresent() && !customerLogin.isPresent()) {
            log.debug("Inside customer.isPresent() && !customerLogin.isPresent()");

            customerLoginRepo.save(cl);
        }

        Optional<Product> productOne = productRepo.findById(p1.getProductId());
        Optional<Product> productTwo = productRepo.findById(p2.getProductId());

        if(!productOne.isPresent())
            productRepo.save(p1);
        if(!productTwo.isPresent())
            productRepo.save(p2);

        slaRepo.saveAll(s);
    }

    @PreDestroy
    public void deleteBeforeStart(){
        slaRepo.deleteAll();
    }
}
