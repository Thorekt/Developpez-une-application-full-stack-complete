package com.thorekt.mdd.microservice.theme_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.thorekt.mdd.microservice.theme_service.model")
public class ThemeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThemeServiceApplication.class, args);
	}

}
