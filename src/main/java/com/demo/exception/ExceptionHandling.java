package com.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.demo.exception.custom.CustomerException;
import com.demo.exception.custom.CustomerLoginException;
import com.demo.util.Utils;

@ControllerAdvice
@RestController
public class ExceptionHandling extends ResponseEntityExceptionHandler{

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest web){
		
		ExceptionMessageFormat exceptionMessage = new ExceptionMessageFormat(e.getMessage(), Utils.getCurrentTimeStamp());
		
		return new ResponseEntity<Object>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(CustomerException.class)
	public final ResponseEntity<Object> handleCustomerExceptions(CustomerException e, WebRequest web){
		
		ExceptionMessageFormat exceptionMessage = new ExceptionMessageFormat(e.getMessage(), Utils.getCurrentTimeStamp());
		
		return new ResponseEntity<Object>(exceptionMessage, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CustomerLoginException.class)
	public final ResponseEntity<Object> handleCustomerLoginExceptions(CustomerLoginException e, WebRequest web){
		
		ExceptionMessageFormat exceptionMessage = new ExceptionMessageFormat(e.getMessage(), Utils.getCurrentTimeStamp());
		
		return new ResponseEntity<Object>(exceptionMessage, HttpStatus.NOT_FOUND);
	}
}
