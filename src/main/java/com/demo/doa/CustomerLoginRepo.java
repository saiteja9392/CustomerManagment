package com.demo.doa;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.demo.model.CustomerLogin;

public interface CustomerLoginRepo extends JpaRepository<CustomerLogin, String>{
	
	CustomerLogin findByLoginid(String id);
	
	@Transactional
	@Modifying
	@Query(value = "update customerlogin set lastlogin = ?1 where loginid = ?2", nativeQuery = true)
	void updateLastLogin(String lastLogin, String uname);
	
	@Transactional
	@Modifying
	@Query(value = "update customerlogin set password = ?2 where loginid = ?1", nativeQuery = true)
	void updatePassword(String uname, String pass);
}
