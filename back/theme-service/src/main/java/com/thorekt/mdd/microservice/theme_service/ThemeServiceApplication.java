package com.thorekt.mdd.microservice.theme_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for the Theme Service
 * 
 * @author Thorekt
 */
@SpringBootApplication
@EntityScan(basePackages = "com.thorekt.mdd.microservice.theme_service.model")
@EnableJpaAuditing
public class ThemeServiceApplication {

	/**
	 * Main method to run the Theme Service application
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ThemeServiceApplication.class, args);
	}

}
