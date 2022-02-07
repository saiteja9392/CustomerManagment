package com.demo.exception;

public class ExceptionMessageFormat {

	private String message;
	private String timeStamp;
	
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
	
	public ExceptionMessageFormat(String message, String timeStamp) {
		super();
		this.message = message;
		this.timeStamp = timeStamp;
	}
		
}
