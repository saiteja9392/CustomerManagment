package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.Customer;
import com.demo.model.CustomerLogin;
import com.demo.service.CustomerServiceImpl;

@RestController
public class CustomerController {
	
	@Autowired(required = true)
	CustomerServiceImpl customerServiceImpl;
	
	
	@GetMapping("/AllCustomers")
	public List<Customer> listAllCustomers(){
		
		return customerServiceImpl.listAllCustomers();
	}
	
	@GetMapping("/AllCustomerLogins")
	public List<CustomerLogin> listAllCustomerLogins(){
		
		return customerServiceImpl.listAllCustomerLogins();
	}
	
	@GetMapping("/GetCustomer/{id}")
	public Customer getCustomer(@PathVariable("id") String username) throws Exception {
		
		return customerServiceImpl.getCustomer(username);
	}
	
	@PostMapping("/AddCustomer")
	public String addCustomer(@RequestBody Customer customer) {
		
		return customerServiceImpl.addCustomer(customer);
	}	
	
	@PostMapping("/UpdateCustomer/{id}")
	public String updateCustomerDetails(@PathVariable("id") String username, @RequestBody Customer customer) {

		return customerServiceImpl.updateCustomerDetails(username,customer);
	}
	
	@PostMapping("/Login/{loginid}/{password}")
	public String login(@PathVariable("loginid") String id, @PathVariable("password") String pass){
		
		return customerServiceImpl.login(id,pass);
	}

	@PostMapping("/AddCustomerLogin")
	public String createCustomerLogin(@RequestBody CustomerLogin customerLogin){
		
		return customerServiceImpl.createCustomerLogin(customerLogin);
	}
	
	@PostMapping("/UpdatePassword/{loginid}/{password}")
	public String updatePassword(@PathVariable("loginid") String username, @PathVariable("password") String password) {
	
		return customerServiceImpl.updatePassword(username,password);
	}
	
	@PostMapping("/DeleteCustomer/{isAdmin}/{loginid}")
	public String deleteCustomer(@PathVariable("isAdmin") String admin, @PathVariable("loginid") String deleteCustomer) {
		
		return customerServiceImpl.deleteCustomer(admin,deleteCustomer);
	}
}
