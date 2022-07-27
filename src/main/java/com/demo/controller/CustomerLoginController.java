package com.demo.controller;

import com.demo.entity.CustomerLogin;
import com.demo.service.CustomerLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/CustomerLogin")
public class CustomerLoginController {

    @Autowired
    private CustomerLoginService customerLoginService;

    @GetMapping("/AllCustomerLogins")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public List<CustomerLogin> listAllCustomerLogins(){

        return customerLoginService.listAllCustomerLogins();
    }

    @GetMapping("/GetCustomerLogin/{id}")
    public CustomerLogin getCustomerLogin(@PathVariable("id") String username) {

        return customerLoginService.getCustomerLogin(username);
    }

    @PostMapping("/AddCustomerLogin")
    public ResponseEntity<CustomerLogin> createCustomerLogin(@RequestBody CustomerLogin customerLogin){

        return customerLoginService.createCustomerLogin(customerLogin);
    }

    @PutMapping("/UpdateCustomer")
    public ResponseEntity<CustomerLogin> updateCustomerDetails(@RequestBody CustomerLogin customerLogin) {

        return customerLoginService.updateCustomerLogin(customerLogin);
    }

    @DeleteMapping("/DeleteCustomer")
    public String deleteCustomer(@RequestParam String adminUser, @RequestParam String deleteCustomer) {

        return customerLoginService.deleteCustomerLogin(adminUser, deleteCustomer);
    }

    @PostMapping("/Login")
    public String login(@RequestParam("loginId") String id, @RequestParam("password") String pass){

        return customerLoginService.login(id,pass);
    }

    @GetMapping("/Decrypt")
    public String decryptedPassword(@RequestParam String adminUser, @RequestParam String username) {

        return customerLoginService.decryptedPassword(adminUser,username);
    }

    @PutMapping("/UpdatePassword")
    public String updatePassword(@RequestParam String username, @RequestParam String password) {

        return customerLoginService.updatePassword(username,password);
    }
}
