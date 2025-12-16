package com.thorekt.mdd.microservice.config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Main application class for the Config Server
 * 
 * @author Thorekt
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	/**
	 * Main method to run the Config Server application
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
