package com.thorekt.mdd.microservice.article_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for the Article Service.
 * 
 * @author Thorekt
 */
@SpringBootApplication
@EntityScan(basePackages = "com.thorekt.mdd.microservice.article_service.model")
@EnableJpaAuditing
public class ArticleServiceApplication {

	/**
	 * Main method to run the Article Service application.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ArticleServiceApplication.class, args);
	}

}
