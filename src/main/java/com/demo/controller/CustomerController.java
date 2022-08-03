package com.demo.controller;

import com.demo.entity.Customer;
import com.demo.response.Response;
import com.demo.service.CustomerService;
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
	CustomerService customerService;
	
	@Autowired
	MessageSource messageSource;
	
	@GetMapping("/AllCustomers")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	@PreAuthorize(value = "hasRole('ADMIN')")
	public List<Customer> listAllCustomers(){
		
		System.out.println(customerService);
		return customerService.listAllCustomers();
	}
	
	@GetMapping("/GetCustomer/{id}")
	public EntityModel<Customer> getCustomer(@PathVariable("id") String username) {
		
		Customer customer = customerService.getCustomer(username);

		EntityModel<Customer> model = EntityModel.of(customer);

		WebMvcLinkBuilder linkToCustomerLoginDetails = linkTo(methodOn(CustomerLoginController.class).getCustomerLogin(username));
		model.add(linkToCustomerLoginDetails.withRel("customer-login-details"));
		
		return model;
	}
	
	@PostMapping("/AddCustomer")
	@PreAuthorize(value = "hasRole('CUSTOMER')")
	public ResponseEntity<Response> addCustomer(@Valid @RequestBody Customer customer) {
		
		return new ResponseEntity<>(customerService.addCustomer(customer),HttpStatus.CREATED);
	}
	
	@PutMapping("/UpdateCustomer")
	public ResponseEntity<Response> updateCustomerDetails(@RequestBody Customer customer) {

		return new ResponseEntity<>(customerService.updateCustomerDetails(customer),HttpStatus.OK);
	}
	
	@DeleteMapping("/DeleteCustomer")
	public ResponseEntity<Response> deleteCustomer(@RequestParam String adminUser, @RequestParam String deleteCustomer) {

		return new ResponseEntity<>(customerService.deleteCustomer(adminUser,deleteCustomer),HttpStatus.OK);
	}
	
	@GetMapping("/Int")
	public String Internationalized() {
		
		return messageSource.getMessage("sample.message", null, LocaleContextHolder.getLocale());
	}
}
