package com.thorekt.mdd.microservice.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the API Gateway
 * 
 * @author Thorekt
 */
@SpringBootApplication
public class ApiGatewayApplication {

	/**
	 * Main method to run the API Gateway application
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
