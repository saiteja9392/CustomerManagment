package com.demo.exception.custom;

@SuppressWarnings("serial")
public class InValidRequestException extends RuntimeException{

	public InValidRequestException(String message) {
		super(message);
	}
}
