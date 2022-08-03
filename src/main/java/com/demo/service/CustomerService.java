package com.demo.service;

import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.exception.custom.InValidRequestException;
import com.demo.exception.custom.ResourceException;
import com.demo.repository.CustomerLoginRepo;
import com.demo.repository.CustomerRepo;
import com.demo.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class CustomerService {
	
	@Autowired
	CustomerRepo repo;

	@Autowired
	CustomerLoginRepo loginRepo;
	
	public List<Customer> listAllCustomers(){

		return repo.findAll();
	}

	public Customer getCustomer(String username) {
		
		Optional<Customer> customer = repo.findById(username);
		
		if(!customer.isPresent()){
			throw new ResourceException("No Customer found with the username");
		}
		
		return customer.get();
	}
	
	public Response addCustomer(Customer customer) {

		Optional<Customer> findCustomer = repo.findById(customer.getId());

		Customer savedCustomer;

		if (!findCustomer.isPresent()) {
			savedCustomer = repo.save(customer);
		} else {
			throw new ResourceException("Customer id already in Use");
		}

		Response response = Response.buildResponse("Customer Added",savedCustomer);

		return response;
	}

	public Response updateCustomerDetails(Customer customer) {
		
		Optional<Customer> c = repo.findById(customer.getId());

		Customer updatedCustomer;
		
		if(!c.isPresent()){
			throw new ResourceException("No Customer Record Found");
		}

		else{
			updatedCustomer = repo.save(customer);
		}

		Response response = Response.buildResponse("Customer Details Updated",updatedCustomer);

		return response;
	}
	
	public Response deleteCustomer(String adminUser,String deleteCustomer) {
		
		Optional<Customer> customer = repo.findById(deleteCustomer);
		Optional<CustomerLogin> customerLogin = loginRepo.findById(deleteCustomer);

		Response response;
		if(!customer.isPresent()) {
			throw new ResourceException("Customer Details not Found");
		}
		
		else{
			
			Optional<CustomerLogin> isAdminUser = loginRepo.findById(adminUser);
			
			if(isAdminUser.isPresent() && isAdminUser.get().isAdmin()) {
			
				repo.delete(customer.get());

				if (customerLogin.isPresent()) {
					loginRepo.delete(customerLogin.get());
				}

				response = Response.buildResponse("Customer details deleted Successfully",customer);
			}
			
			else if(!isAdminUser.isPresent()) {
				throw new InValidRequestException("ADMIN CustomerLogin Details Not Found");
			}

			else {
				throw new InValidRequestException("Customer is not an ADMIN User");
			}
		}
		
		return response;
	}
	
}
