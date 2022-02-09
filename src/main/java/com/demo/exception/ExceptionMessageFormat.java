package com.demo.exception;

public class ExceptionMessageFormat {

	private String message;
	private String timeStamp;
	private String details;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	public ExceptionMessageFormat(String message, String timeStamp,String details) {
		super();
		this.message = message;
		this.timeStamp = timeStamp;
		this.details = details;
	}
		
}
