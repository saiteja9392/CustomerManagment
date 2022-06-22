package com.demo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.Customer;
import com.demo.model.CustomerLogin;
import com.demo.service.CustomerServiceImpl;

@RestController
public class CustomerController {
	
	@Autowired
	CustomerServiceImpl customerServiceImpl;
	
	@Autowired
	MessageSource messageSource;
	
	@GetMapping("/AllCustomers")
	public List<Customer> listAllCustomers(){
		
		return customerServiceImpl.listAllCustomers();
	}
	
	@GetMapping("/AllCustomerLogins")
	public List<CustomerLogin> listAllCustomerLogins(){
		
		return customerServiceImpl.listAllCustomerLogins();
	}
	
	@GetMapping("/GetCustomer/{id}")
	public EntityModel<Customer> getCustomer(@PathVariable("id") String username) throws Exception {
		
		Customer customer = customerServiceImpl.getCustomer(username);
		
		EntityModel<Customer> model = EntityModel.of(customer);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listAllCustomers());
		model.add(linkTo.withRel("all-customers"));
		
		return model;
	}
	
	@PostMapping("/GetCustomer")
	public Customer getAuthCustomer(@RequestBody Customer customer) throws Exception {
		
		return customerServiceImpl.getCustomer(customer.getId());
	}
	
	@GetMapping("/GetCustomerLogin/{id}")
	public CustomerLogin getCustomerLogin(@PathVariable("id") String username) throws Exception {
		
		return customerServiceImpl.getCustomerLogin(username);
	}
	
	@PostMapping("/AddCustomer")
	public String addCustomer(@Valid @RequestBody Customer customer) {
		
		return customerServiceImpl.addCustomer(customer);
	}	
	
	@PutMapping("/UpdateCustomer/{id}")
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
	
	@PutMapping("/UpdatePassword/{loginid}/{password}")
	public String updatePassword(@PathVariable("loginid") String username, @PathVariable("password") String password) {
	
		return customerServiceImpl.updatePassword(username,password);
	}
	
	@DeleteMapping("/DeleteCustomer/{isAdmin}/{loginid}")
	public String deleteCustomer(@PathVariable("isAdmin") String admin, @PathVariable("loginid") String deleteCustomer) {
		
		return customerServiceImpl.deleteCustomer(admin,deleteCustomer);
	}
	
	@GetMapping("/Decrypt/{isAdmin}/{loginid}")
	public String decryptedPassword(@PathVariable("isAdmin") String isAdmin, @PathVariable("loginid") String username) {
		
		return customerServiceImpl.decryptedPassword(isAdmin,username);
	}
	
	@GetMapping("/Int")
	public String Internationalized() {
		
		return messageSource.getMessage("sample.message", null, LocaleContextHolder.getLocale());
	}
}
