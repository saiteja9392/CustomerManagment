package com.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.doa.CustomerLoginRepo;
import com.demo.doa.CustomerRepo;
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
	
	public Customer getCustomer(String username) throws Exception {
		
		Customer customer = repo.findById(username);
		
		if(customer == null){
			throw new Exception("No Customer found with the username");
		}
		
		return customer;
	}
	
	public String addCustomer(Customer customer) {
		
		Customer findCustomer = repo.findById(customer.getId());
		
		if(findCustomer == null) {
			repo.save(customer);
			Status = "Customer Added Successfully";
		}
		else {
			Status = "Customer id already in Use";
		}
		
		return Status;
	}

	public String updateCustomerDetails(String username, Customer customer) {
		
		Customer c = repo.findById(username);
		
		if(c == null){
			Status = "No Customer Record Found";
		}

		else{
			
			if (!c.getId().contentEquals(customer.getId())) {
				Status = "Both Id's are different, please check";
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
				Status = "User Login Not Found";
			}

		}

		else if (user == null) {
			Status = "User Login Not Found";
		}
		
		return Status;
	}

	public String createCustomerLogin(String username, String password){
		
		Customer c = repo.findById(username);
		
		if(c == null) {
			
			Status = "Customer Details not Found";
		}
		
		else{
			
			CustomerLogin user = loginrepo.findByLoginid(username);
			
			
			if(user == null) {
				
				loginrepo.addCustomerLogin(username, AES.encrypt(password));
				Status = "CustomerLogin Added Successfully";
			}
			
			else if (user.getLoginid().contentEquals(username)) {
	
				Status = "CustomerLogin Already exsists with this username";
			}
		}
		
		return Status;
	}

	public String updatePassword(String username, String password) {

		CustomerLogin user = loginrepo.findByLoginid(username);

		if (user != null) {
			if (user.getLoginid().contentEquals(username)) {
				
				loginrepo.updatePassword(username, password);
				Status = "Password updated Successfully";
				
			}
			else {
				Status = "CustomerLogin Login Not Found";
			}
		}
		else {
			Status = "CustomerLogin Login Not Found";
		}
		
		return Status;
	}
	
	public String deleteCustomer(String username) {
		
		Customer c = repo.findById(username);
		CustomerLogin user = loginrepo.findByLoginid(username);
		
		if(c == null) {
			Status = "Customer Details not Found";
		}
		
		if(c != null) {
			repo.delete(c);
			
			if(user != null) {
				loginrepo.delete(user);
			}
			
			Status = "Customer details deleted Successfully";
		}
		
		return Status;
	}
	
}
