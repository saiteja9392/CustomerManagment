package com.demo.exception.custom;

public class CustomerLoginException extends RuntimeException {

	public CustomerLoginException(String message) {
		super(message);
	}
	
}
