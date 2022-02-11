package com.demo.exception.custom;

@SuppressWarnings("serial")
public class ResourceException extends RuntimeException{

	public ResourceException(String message) {
		super(message);
	}

}
