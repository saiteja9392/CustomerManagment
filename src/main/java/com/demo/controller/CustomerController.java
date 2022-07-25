package com.demo.controller;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/Customer")
public class CustomerController {
	
	@Autowired
	CustomerServiceImpl customerServiceImpl;
	
	@Autowired
	MessageSource messageSource;
	
	@GetMapping("/AllCustomers")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	@PreAuthorize(value = "hasRole('ADMIN')")
	public List<Customer> listAllCustomers(){
		
		System.out.println(customerServiceImpl);
		return customerServiceImpl.listAllCustomers();
	}
	
	@GetMapping("/AllCustomerLogins")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public List<CustomerLogin> listAllCustomerLogins(){
		
		return customerServiceImpl.listAllCustomerLogins();
	}
	
	@GetMapping("/GetCustomer/{id}")
	public EntityModel<Customer> getCustomer(@PathVariable("id") String username) {
		
		Customer customer = customerServiceImpl.getCustomer(username);
		
		EntityModel<Customer> model = EntityModel.of(customer);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listAllCustomers());
		model.add(linkTo.withRel("all-customers"));
		
		return model;
	}
	
	@PostMapping("/GetCustomer")
	public Customer getAuthCustomer(@RequestBody Customer customer) {
		
		return customerServiceImpl.getCustomer(customer.getId());
	}
	
	@GetMapping("/GetCustomerLogin/{id}")
	public CustomerLogin getCustomerLogin(@PathVariable("id") String username) {
		
		return customerServiceImpl.getCustomerLogin(username);
	}
	
	@PostMapping("/AddCustomer")
	@PreAuthorize(value = "hasRole('CUSTOMER')")
	public String addCustomer(@Valid @RequestBody Customer customer) {
		
		return customerServiceImpl.addCustomer(customer);
	}	
	
	@PutMapping("/UpdateCustomer")
	public String updateCustomerDetails(@RequestBody Customer customer) {

		return customerServiceImpl.updateCustomerDetails(customer);
	}
	
	@PostMapping("/Login")
	public String login(@RequestParam("loginId") String id, @RequestParam("password") String pass){
		
		return customerServiceImpl.login(id,pass);
	}

	@PostMapping("/AddCustomerLogin")
	public String createCustomerLogin(@RequestBody CustomerLogin customerLogin){
		
		return customerServiceImpl.createCustomerLogin(customerLogin);
	}
	
	@PutMapping("/UpdatePassword")
	public String updatePassword(@RequestParam String username, @RequestParam String password) {
	
		return customerServiceImpl.updatePassword(username,password);
	}
	
	@DeleteMapping("/DeleteCustomer")
	public String deleteCustomer(@RequestParam String adminUser, @RequestParam String deleteCustomer) {
		
		return customerServiceImpl.deleteCustomer(adminUser,deleteCustomer);
	}
	
	@GetMapping("/Decrypt")
	public String decryptedPassword(@RequestParam String adminUser, @RequestParam String username) {
		
		return customerServiceImpl.decryptedPassword(adminUser,username);
	}
	
	@GetMapping("/Int")
	public String Internationalized() {
		
		return messageSource.getMessage("sample.message", null, LocaleContextHolder.getLocale());
	}
}
