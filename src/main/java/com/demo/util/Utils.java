package com.demo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

	public static String getCurrentTimeStamp() {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		return dtf.format(now);
	}
	
	public static String getOrderDate() {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		
		return dtf.format(now);
	}

	public static String getRefundTransactionId(){

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		return "REF"+dtf.format(now);
	}
}
