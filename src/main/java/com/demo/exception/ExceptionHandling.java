package com.demo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.demo.exception.custom.InValidRequestException;
import com.demo.exception.custom.ResourceException;
import com.demo.util.Utils;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler{

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest web){
		
		ExceptionMessageFormat exceptionMessage = new ExceptionMessageFormat(e.getMessage(), Utils.getCurrentTimeStamp(),web.getDescription(false));
		
		return new ResponseEntity<Object>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceException.class)
	public final ResponseEntity<Object> handleCustomerExceptions(ResourceException e, WebRequest web){
		
		ExceptionMessageFormat exceptionMessage = new ExceptionMessageFormat(e.getMessage(), Utils.getCurrentTimeStamp(),web.getDescription(false));
		
		return new ResponseEntity<Object>(exceptionMessage, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InValidRequestException.class)
	public final ResponseEntity<Object> handleCustomerLoginExceptions(InValidRequestException e, WebRequest web){
		
		ExceptionMessageFormat exceptionMessage = new ExceptionMessageFormat(e.getMessage(), Utils.getCurrentTimeStamp(),web.getDescription(false));
		
		return new ResponseEntity<Object>(exceptionMessage, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ExceptionMessageFormat exceptionMessage = new ExceptionMessageFormat("Validation Failed", Utils.getCurrentTimeStamp(),ex.getBindingResult().toString());
		
		return new ResponseEntity<Object>(exceptionMessage, HttpStatus.BAD_REQUEST);
	}
}
