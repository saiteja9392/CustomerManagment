package com.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomerSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	PasswordEncoder encoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/Customer/*").hasRole("CUSTOMER")
			.antMatchers(HttpMethod.POST, "/Customer/*").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/Customer/*").hasRole("ADMIN")
			.antMatchers(HttpMethod.DELETE, "/Customer/*").hasRole("ADMIN")
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic();
	}
	
	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
	  
		  UserDetails adminrole = User.builder().username("admin")
				  								.password(encoder.encode("admin")) 
				  								.roles("ADMIN")
				  								.build();
		  
		  UserDetails customerrole = User.builder().username("customer")
												.password(encoder.encode("customer")) 
												.roles("CUSTOMER")
												.build();
												  
		  return new InMemoryUserDetailsManager(adminrole,customerrole); 
		  
	}
	
}
