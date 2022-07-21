package com.demo.doa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.demo.entity.CustomerLogin;


public interface CustomerLoginRepo extends JpaRepository<CustomerLogin, String>{

}
