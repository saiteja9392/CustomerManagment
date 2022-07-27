package com.demo.service;

import com.demo.doa.CustomerLoginRepo;
import com.demo.doa.CustomerRepo;
import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.exception.custom.InValidRequestException;
import com.demo.exception.custom.ResourceException;
import com.demo.util.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerLoginService {

    @Autowired
    CustomerRepo repo;

    @Autowired
    CustomerLoginRepo loginRepo;

    public String Status = "";

    public List<CustomerLogin> listAllCustomerLogins(){

        return loginRepo.findAll();
    }

    public CustomerLogin getCustomerLogin(String username) {

        Optional<CustomerLogin> customerLogin = loginRepo.findById(username);

        if(!customerLogin.isPresent()){
            throw new ResourceException("No Customer Login found with the username");
        }

        return customerLogin.get();
    }

    public ResponseEntity<CustomerLogin> createCustomerLogin(CustomerLogin customerLogin){

        CustomerLogin savedCustomerLogin = null;
        
        Optional<Customer> c = repo.findById(customerLogin.getLoginid());

        if(!c.isPresent()) {

            throw new ResourceException("Customer Details not Found");
        }

        else{

            Optional<CustomerLogin> user = loginRepo.findById(customerLogin.getLoginid());

            if(!user.isPresent()) {

                CustomerLogin cLogin = new CustomerLogin();

                cLogin.setLoginid(customerLogin.getLoginid());
                cLogin.setPassword(AES.encrypt(customerLogin.getPassword()));
                cLogin.setAdmin(customerLogin.isAdmin());

                savedCustomerLogin = loginRepo.save(cLogin);
            }

            else if (customerLogin.getLoginid().contentEquals(user.get().getLoginid())) {

                throw new InValidRequestException("CustomerLogin Already exists with this username");
            }
        }

        return new ResponseEntity<>(savedCustomerLogin, HttpStatus.CREATED);
    }

    public String login(String username, String password){

        Optional<CustomerLogin> user = loginRepo.findById(username);

        if (user.isPresent()) {

            if (password.contentEquals(AES.decrypt(user.get().getPassword()))) {

                Status = "Login Success";

                user.get().setLastlogin(new Date());

                loginRepo.save(user.get());

            } else {
                throw new InValidRequestException("Login Failed");
            }
        }

        else {
            throw new InValidRequestException("User Login Not Found");
        }

        return Status;
    }

    public String decryptedPassword(String adminUser, String username) {

        Optional<Customer> customer = repo.findById(username);
        Optional<CustomerLogin> customerLogin = loginRepo.findById(username);

        if(!customer.isPresent()) {
            throw new ResourceException("Customer Details not Found");
        }

        else {

            if(!customerLogin.isPresent()) {
                throw new ResourceException("CustomerLogin Details not Found");
            }

            else {
                Optional<CustomerLogin> isAdminUser = loginRepo.findById(adminUser);

                if (isAdminUser.isPresent() && isAdminUser.get().isAdmin()) {

                    String decryptedPassword = AES.decrypt(customerLogin.get().getPassword());

                    Status = "DecryptedPassword for " + customerLogin.get().getLoginid() + " is " + decryptedPassword;
                }
                else {
                    throw new InValidRequestException("Customer is not an ADMIN User");
                }
            }
        }

        return Status;
    }

    public String updatePassword(String username, String password) {

        Optional<CustomerLogin> user = loginRepo.findById(username);

        if (user.isPresent()) {

            if(AES.decrypt(user.get().getPassword()).contentEquals(password)) {

                throw new InValidRequestException("Old and New Password are same");
            }
            else {

                user.get().setPassword(AES.encrypt(password));

                loginRepo.save(user.get());

                Status = "Password updated Successfully";
            }
        }
        else {
            throw new ResourceException("CustomerLogin Login Not Found");
        }

        return Status;
    }

    public ResponseEntity<CustomerLogin> updateCustomerLogin(CustomerLogin customerLogin) {

        Optional<CustomerLogin> c = loginRepo.findById(customerLogin.getLoginid());

        if(!c.isPresent()){
            throw new ResourceException("No Customer Login Record Found");
        }

        else{
            loginRepo.save(c.get());
        }

        return new ResponseEntity<>(customerLogin, HttpStatus.CREATED);
    }

    public String deleteCustomerLogin(String adminUser, String deleteCustomer) {

        Optional<CustomerLogin> customerLogin = loginRepo.findById(deleteCustomer);

        if(!customerLogin.isPresent()) {
            throw new ResourceException("Customer Login Details not Found");
        }

        else{

            Optional<CustomerLogin> admin = loginRepo.findById(adminUser);

            if(admin.isPresent() && admin.get().isAdmin()) {
                loginRepo.delete(customerLogin.get());
                Status = "Customer Login Record Delete";
            }
            else if(!admin.isPresent()) {
                throw new InValidRequestException("ADMIN CustomerLogin Details Not Found");
            }
            else
                throw new ResourceException("Customer Login Not ADMIN User");
        }

        return Status;
    }
}
