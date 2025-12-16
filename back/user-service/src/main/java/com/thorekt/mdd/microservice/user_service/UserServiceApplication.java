package com.thorekt.mdd.microservice.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for the User Service
 * 
 * @author Thorekt
 */
@SpringBootApplication
@EntityScan(basePackages = "com.thorekt.mdd.microservice.user_service.model")
@EnableJpaAuditing
public class UserServiceApplication {

	/**
	 * Main method to run the User Service application
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
