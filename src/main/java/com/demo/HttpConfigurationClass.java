package com.demo;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * This  class is to expose the Http Trace details via actuator 
 */

@Configuration
public class HttpConfigurationClass {
 
	@Bean
	public HttpTraceRepository httpTrace() {
		
		return new InMemoryHttpTraceRepository();
	}
}
