package com.demo.service;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.exception.custom.InValidRequestException;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import com.demo.repository.wallet.WalletTransactionRepo;
import com.demo.response.Response;
import com.demo.util.AES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerLoginService {

    @Autowired
    CustomerRepo repo;

    @Autowired
    CustomerLoginRepo loginRepo;

    @Autowired
    WalletTransactionRepo walletTransactionRepo;

    public List<CustomerLogin> listAllCustomerLogins(){

        return loginRepo.findAll();
    }

    public CustomerLogin getCustomerLogin(String username) {

        Optional<CustomerLogin> customerLogin = loginRepo.findById(username);

        if(!customerLogin.isPresent()){
            throw new ResourceException("No Customer Login Found With The Username");
        }

        return customerLogin.get();
    }

    public Response createCustomerLogin(CustomerLogin customerLogin){

        CustomerLogin savedCustomerLogin;
        
        Optional<Customer> c = repo.findById(customerLogin.getLoginid());

        Response response = null;

        if(!c.isPresent()) {

            throw new ResourceException("Customer Details Not Found");
        }

        else{

            Optional<CustomerLogin> user = loginRepo.findById(customerLogin.getLoginid());

            if(!user.isPresent()) {

                CustomerLogin cLogin = new CustomerLogin();

                cLogin.setLoginid(customerLogin.getLoginid());
                cLogin.setPassword(AES.encrypt(customerLogin.getPassword()));
                cLogin.setAdmin(customerLogin.isAdmin());

                savedCustomerLogin = loginRepo.save(cLogin);

                response = Response.buildResponse("Customer Login Added",savedCustomerLogin);
            }

            else if (customerLogin.getLoginid().contentEquals(user.get().getLoginid())) {

                throw new InValidRequestException("CustomerLogin Already Exists With This Username");
            }
        }

        return response;
    }

    public Response login(String username, String password){

        Optional<CustomerLogin> user = loginRepo.findById(username);

        Response response;

        if (user.isPresent()) {

            if (password.contentEquals(AES.decrypt(user.get().getPassword()))) {

                response = Response.buildResponse("Login Success",null);

                user.get().setLastlogin(new Date());

                loginRepo.save(user.get());

            } else {
                throw new InValidRequestException("Login Failed");
            }
        }

        else {
            throw new InValidRequestException("Customer Login Not Found");
        }

        return response;
    }

    public Response decryptedPassword(String adminUser, String username) {

        Optional<Customer> customer = repo.findById(username);
        Optional<CustomerLogin> customerLogin = loginRepo.findById(username);

        Response response;

        if(!customer.isPresent()) {
            throw new ResourceException("Customer Details Not Found");
        }

        else {

            if(!customerLogin.isPresent()) {
                throw new ResourceException("CustomerLogin Details Not Found");
            }

            else {
                Optional<CustomerLogin> isAdminUser = loginRepo.findById(adminUser);

                if (isAdminUser.isPresent() && isAdminUser.get().isAdmin()) {

                    String decryptedPassword = AES.decrypt(customerLogin.get().getPassword());

                    response = Response.buildResponse(String.format("Decrypted Password for '%s' is '%s'",
                            customerLogin.get().getLoginid(),decryptedPassword),
                            null);
                }
                else {
                    throw new InValidRequestException("Customer Is Not An ADMIN User");
                }
            }
        }

        return response;
    }

    public Response updatePassword(String username, String password) {

        Optional<CustomerLogin> user = loginRepo.findById(username);

        Response response;

        if (user.isPresent()) {

            if(AES.decrypt(user.get().getPassword()).contentEquals(password)) {

                throw new InValidRequestException("Old And New Password Are Same");
            }
            else {

                user.get().setPassword(AES.encrypt(password));

                loginRepo.save(user.get());

                response = Response.buildResponse("Password updated Successfully",null);
            }
        }
        else {
            throw new ResourceException("Customer Login Not Found");
        }

        return response;
    }

    public Response updateCustomerLogin(CustomerLogin customerLogin) {

        Optional<CustomerLogin> c = loginRepo.findById(customerLogin.getLoginid());

        if (!c.isPresent()) {
            throw new ResourceException("No Customer Login Record Found");
        }

        CustomerLogin updateCustomerLogin = loginRepo.save(c.get());

        return Response.buildResponse("Customer Login Updated", updateCustomerLogin);
    }

    @Transactional
    public Response deleteCustomerLogin(String adminUser, String deleteCustomer) {

        Optional<CustomerLogin> customerLogin = loginRepo.findById(deleteCustomer);

        Response response;

        if(!customerLogin.isPresent()) {
            throw new ResourceException("Customer Login Details Not Found");
        }

        else{

            Optional<CustomerLogin> admin = loginRepo.findById(adminUser);

            if(admin.isPresent() && admin.get().isAdmin()) {

                loginRepo.delete(customerLogin.get());
                int numberOfDeletedWalletTransactions = this.deleteCustomerWalletTransactions(deleteCustomer);
                log.info(String.valueOf(numberOfDeletedWalletTransactions));

                response = Response.buildResponse("Customer Login Details Deleted",customerLogin);
            }
            else if(!admin.isPresent()) {
                throw new InValidRequestException("Admin Details Not Found");
            }
            else
                throw new ResourceException("Customer Login Not ADMIN User");
        }

        return response;
    }

    @Transactional
    public int deleteCustomerWalletTransactions(String deleteCustomer) {

        return walletTransactionRepo.deleteCustomerWalletTransactions(deleteCustomer);
    }
}
