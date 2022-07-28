package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableWebSecurity
@Slf4j
public class CustomerApplication {

	public static void main(String[] args) {
		log.info("Inside CustomerApplication main method");

		SpringApplication.run(CustomerApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		log.info("Inside PasswordEncoder method");

		return new BCryptPasswordEncoder();
	}

}
