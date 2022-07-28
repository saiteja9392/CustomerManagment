package com.demo.repository;

import com.demo.entity.CustomerLogin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerLoginRepo extends JpaRepository<CustomerLogin, String>{

}
