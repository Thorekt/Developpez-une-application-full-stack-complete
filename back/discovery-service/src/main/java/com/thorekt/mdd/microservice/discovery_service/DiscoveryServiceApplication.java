package com.thorekt.mdd.microservice.discovery_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main application class for the Discovery Service
 * 
 * @author Thorekt
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

	/**
	 * Main method to run the Discovery Service application
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServiceApplication.class, args);
	}

}
