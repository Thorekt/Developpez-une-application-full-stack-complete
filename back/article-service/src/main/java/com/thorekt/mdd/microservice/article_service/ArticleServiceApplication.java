package com.thorekt.mdd.microservice.article_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.thorekt.mdd.microservice.article_service.model")
public class ArticleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticleServiceApplication.class, args);
	}

}
