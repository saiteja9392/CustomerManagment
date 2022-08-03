package com.demo.controller;

import com.demo.entity.CustomerLogin;
import com.demo.response.Response;
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
    public ResponseEntity<Response> createCustomerLogin(@RequestBody CustomerLogin customerLogin){

        return new ResponseEntity<>(customerLoginService.createCustomerLogin(customerLogin),HttpStatus.CREATED);
    }

    @PutMapping("/UpdateCustomerLogin")
    public ResponseEntity<Response> updateCustomerDetails(@RequestBody CustomerLogin customerLogin) {

        return new ResponseEntity<>(customerLoginService.updateCustomerLogin(customerLogin),HttpStatus.OK);
    }

    @DeleteMapping("/DeleteCustomerLogin")
    public ResponseEntity<Response> deleteCustomer(@RequestParam String adminUser, @RequestParam String deleteCustomer) {

        return new ResponseEntity<>(customerLoginService.deleteCustomerLogin(adminUser, deleteCustomer),HttpStatus.OK);
    }

    @PostMapping("/Login")
    public ResponseEntity<Response> login(@RequestParam("loginId") String id, @RequestParam("password") String pass){

        return new ResponseEntity<>(customerLoginService.login(id,pass),HttpStatus.OK);
    }

    @GetMapping("/Decrypt")
    public ResponseEntity<Response> decryptedPassword(@RequestParam String adminUser, @RequestParam String username) {

        return new ResponseEntity<>(customerLoginService.decryptedPassword(adminUser,username),HttpStatus.OK);
    }

    @PutMapping("/UpdatePassword")
    public ResponseEntity<Response> updatePassword(@RequestParam String username, @RequestParam String password) {

        return new ResponseEntity<>(customerLoginService.updatePassword(username,password),HttpStatus.OK);
    }
}
