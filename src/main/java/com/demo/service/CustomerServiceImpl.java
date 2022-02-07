package com.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.doa.CustomerLoginRepo;
import com.demo.doa.CustomerRepo;
import com.demo.exception.custom.CustomerException;
import com.demo.exception.custom.CustomerLoginException;
import com.demo.model.Customer;
import com.demo.model.CustomerLogin;
import com.demo.util.AES;
import com.demo.util.Utils;

@Service
public class CustomerServiceImpl {

	private String Status = "";
	
	@Autowired(required = true)
	CustomerRepo repo;
	
	@Autowired(required = true)
	CustomerLoginRepo loginrepo;
	
	public List<Customer> listAllCustomers(){
		
		List<Customer> list = new ArrayList<Customer>();
		
		list = repo.findAll();
		
		return list;
		
	}
	
	public List<CustomerLogin> listAllCustomerLogins(){
		
		List<CustomerLogin> list = new ArrayList<CustomerLogin>();
		
		list = loginrepo.findAll();
		
		return list;
		
	}
	
	public Customer getCustomer(String username) throws Exception {
		
		Customer customer = repo.findById(username);
		
		if(customer == null){
			throw new CustomerException("No Customer found with the username");
		}
		
		return customer;
	}
	
	public CustomerLogin getCustomerLogin(String username) throws Exception {
		
		CustomerLogin customerLogin = loginrepo.findByLoginid(username);
		
		if(customerLogin == null){
			throw new CustomerLoginException("No Customer Login found with the username");
		}
		
		return customerLogin;
	}
	
	public String addCustomer(Customer customer) {
		
		Customer findCustomer = repo.findById(customer.getId());
		
		if(findCustomer == null) {
			repo.save(customer);
			Status = "Customer Added Successfully";
		}
		else {
			throw new CustomerException("Customer id already in Use");
		}
		
		return Status;
	}

	public String updateCustomerDetails(String username, Customer customer) {
		
		Customer c = repo.findById(username);
		
		if(c == null){
			throw new CustomerException("No Customer Record Found");
		}

		else{
			
			if (!c.getId().contentEquals(customer.getId())) {
				throw new CustomerException("Both Id's are different, please check");
			} 
			
			else {

				if (c.getId().contentEquals(customer.getId()) && c.getFirstname().contentEquals(customer.getFirstname())
						&& c.getLastname().contentEquals(customer.getLastname())) {

					Status = "No update in Customer Record";
				}

				else {

					repo.save(customer);
					Status = "Customer Record Updated Successfully";
				}
			}

		}
	
		return Status;
	}

	public String login(String username, String password){
		
		String timeStamp = Utils.getCurrentTimeStamp();
		
		CustomerLogin user = loginrepo.findByLoginid(username);

		if (user != null) {
			if (user.getLoginid().contentEquals(username)) {

				if (password.contentEquals(AES.decrypt(user.getPassword()))) {

					Status = "Login Success";
					
					user.setLastlogin(timeStamp);
					user.setLoginid(username);
					
					loginrepo.updateLastLogin(user.getLastlogin(),user.getLoginid());
					
				} else {
					Status = "Login failed";
				}
			}

			else {
				throw new CustomerLoginException("User Login Not Found");
			}

		}

		else if (user == null) {
			throw new CustomerLoginException("User Login Not Found");
		}
		
		return Status;
	}

	public String createCustomerLogin(CustomerLogin customerLogin){
		
		Customer c = repo.findById(customerLogin.getLoginid());
		
		if(c == null) {
			
			throw new CustomerException("Customer Details not Found");
		}
		
		else{
			
			CustomerLogin user = loginrepo.findByLoginid(customerLogin.getLoginid());
			
			if(user == null) {
				
				CustomerLogin cLogin = new CustomerLogin();
				
				cLogin.setLoginid(customerLogin.getLoginid());
				cLogin.setPassword(AES.encrypt(customerLogin.getPassword()));
				cLogin.setIsAdmin(customerLogin.getIsAdmin());
				
				if(customerLogin.getIsAdmin() == "")
					cLogin.setIsAdmin("NULL");
				
				loginrepo.save(cLogin);
				
				Status = "CustomerLogin Added Successfully";
			}
			
			else if (customerLogin.getLoginid().contentEquals(user.getLoginid())) {
	
				throw new CustomerLoginException("CustomerLogin Already exsists with this username");
			}
		}
		
		return Status;
	}

	public String updatePassword(String username, String password) {

		CustomerLogin user = loginrepo.findByLoginid(username);

		if (user != null) {
			if (user.getLoginid().contentEquals(username)) {
				
				loginrepo.updatePassword(username, AES.encrypt(password));
				Status = "Password updated Successfully";
				
			}
			else {
				throw new CustomerLoginException("CustomerLogin Login Not Found");
			}
		}
		else {
			throw new CustomerLoginException("CustomerLogin Login Not Found");
		}
		
		return Status;
	}
	
	public String deleteCustomer(String admin,String deleteCustomer) {
		
		Customer customer = repo.findById(deleteCustomer);
		CustomerLogin customerLogin = loginrepo.findByLoginid(deleteCustomer);
		
		if(customer == null) {
			throw new CustomerException("Customer Details not Found");
		}
		
		else{
			
			CustomerLogin isAdminUser = loginrepo.findByLoginid(admin);
			
			if(isAdminUser != null && isAdminUser.getIsAdmin().contentEquals("A")) {
			
				repo.delete(customer);

				if (customerLogin != null) {
					loginrepo.delete(customerLogin);
				}

				Status = "Customer details deleted Successfully";
			}
			
			else if(isAdminUser == null) {
				throw new CustomerException("ADMIN CustomerLogin Details Not Found");
			}
			
			else {
				throw new CustomerException("Customer is not an ADMIN User");
			}
		}
		
		return Status;
	}

	public String decryptedPassword(String isAdmin, String username) {
		
		Customer customer = repo.findById(username);
		CustomerLogin customerLogin = loginrepo.findByLoginid(username);
		
		if(customer == null) {
			throw new CustomerException("Customer Details not Found");
		}
		
		else {
			
			if(customerLogin == null) {
				throw new CustomerLoginException("CustomerLogin Details not Found");
			}
			
			else {
				CustomerLogin isAdminUser = loginrepo.findByLoginid(isAdmin);

				if (isAdminUser != null && isAdminUser.getIsAdmin().contentEquals("A")) {

					String decryptedPassword = AES.decrypt(customerLogin.getPassword());

					Status = "DecryptedPassword for " + customerLogin.getLoginid() + " is " + decryptedPassword;
				}
			}
		}
		
		return Status;
	}
	
}
