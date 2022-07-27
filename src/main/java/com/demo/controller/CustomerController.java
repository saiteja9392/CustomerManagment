package com.demo.controller;

import com.demo.entity.Customer;
import com.demo.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	@GetMapping("/GetCustomer/{id}")
	public EntityModel<Customer> getCustomer(@PathVariable("id") String username) {
		
		Customer customer = customerServiceImpl.getCustomer(username);
		
		EntityModel<Customer> model = EntityModel.of(customer);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listAllCustomers());
		model.add(linkTo.withRel("all-customers"));
		
		return model;
	}
	
	@PostMapping("/AddCustomer")
	@PreAuthorize(value = "hasRole('CUSTOMER')")
	public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer) {
		
		return customerServiceImpl.addCustomer(customer);
	}
	
	@PutMapping("/UpdateCustomer")
	public ResponseEntity<Customer> updateCustomerDetails(@RequestBody Customer customer) {

		return customerServiceImpl.updateCustomerDetails(customer);
	}
	
	@DeleteMapping("/DeleteCustomer")
	public String deleteCustomer(@RequestParam String adminUser, @RequestParam String deleteCustomer) {

		return customerServiceImpl.deleteCustomer(adminUser, deleteCustomer);
	}
	
	@GetMapping("/Int")
	public String Internationalized() {
		
		return messageSource.getMessage("sample.message", null, LocaleContextHolder.getLocale());
	}
}
