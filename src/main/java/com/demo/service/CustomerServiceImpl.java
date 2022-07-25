package com.demo.service;

import com.demo.doa.CustomerLoginRepo;
import com.demo.doa.CustomerRepo;
import com.demo.entity.Customer;
import com.demo.entity.CustomerLogin;
import com.demo.exception.custom.InValidRequestException;
import com.demo.exception.custom.ResourceException;
import com.demo.util.AES;
import com.demo.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class CustomerServiceImpl {
	
	public String Status = "";
	
	@Autowired
	CustomerRepo repo;
	
	@Autowired
	CustomerLoginRepo loginRepo;
	
	public List<Customer> listAllCustomers(){

		return repo.findAll();
	}
	
	public List<CustomerLogin> listAllCustomerLogins(){

		return loginRepo.findAll();
	}
	
	public Customer getCustomer(String username) {
		
		Optional<Customer> customer = repo.findById(username);
		
		if(!customer.isPresent()){
			throw new ResourceException("No Customer found with the username");
		}
		
		return customer.get();
	}
	
	public CustomerLogin getCustomerLogin(String username) {
		
		Optional<CustomerLogin> customerLogin = loginRepo.findById(username);
		
		if(!customerLogin.isPresent()){
			throw new ResourceException("No Customer Login found with the username");
		}
		
		return customerLogin.get();
	}
	
	public String addCustomer(Customer customer) {
		
		Optional<Customer> findCustomer = repo.findById(customer.getId());
		
		if(!findCustomer.isPresent()) {
			repo.save(customer);
			Status = "Customer Added Successfully";
		}
		else {
			throw new ResourceException("Customer id already in Use");
		}
		
		return Status;
	}

	public String updateCustomerDetails(Customer customer) {
		
		Optional<Customer> c = repo.findById(customer.getId());
		
		if(!c.isPresent()){
			throw new ResourceException("No Customer Record Found");
		}

		else{
			repo.save(customer);
			Status = "Customer Record Saved Successfully";
		}
	
		return Status;
	}

	public String login(String username, String password){
		
		String timeStamp = Utils.getCurrentTimeStamp();
		
		Optional<CustomerLogin> user = loginRepo.findById(username);

		if (user.isPresent()) {

			if (password.contentEquals(AES.decrypt(user.get().getPassword()))) {

				Status = "Login Success";

				user.get().setLastlogin(timeStamp);

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

	public String createCustomerLogin(CustomerLogin customerLogin){
		
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
				
				loginRepo.save(cLogin);
				
				Status = "CustomerLogin Added Successfully";
			}
			
			else if (customerLogin.getLoginid().contentEquals(user.get().getLoginid())) {
	
				throw new InValidRequestException("CustomerLogin Already exists with this username");
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
	
	public String deleteCustomer(String adminUser,String deleteCustomer) {
		
		Optional<Customer> customer = repo.findById(deleteCustomer);
		Optional<CustomerLogin> customerLogin = loginRepo.findById(deleteCustomer);
		
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

				Status = "Customer details deleted Successfully";
			}
			
			else if(!isAdminUser.isPresent()) {
				throw new InValidRequestException("ADMIN CustomerLogin Details Not Found");
			}

			else {
				throw new InValidRequestException("Customer is not an ADMIN User");
			}
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
	
}
