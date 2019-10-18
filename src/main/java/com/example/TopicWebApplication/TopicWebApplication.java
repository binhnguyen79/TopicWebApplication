package com.example.TopicWebApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.example.TopicWebApplication.configuration, "
		+ "com.example.TopicWebApplication.services, "
		+ "com.example.TopicWebApplication.jwt, "
		+ "com.example.TopicWebApplication.message.*,"
		+ "com.example.TopicWebApplication.controller")
@EntityScan("com.example.TopicWebApplication.model")
@EnableJpaRepositories("com.example.TopicWebApplication.repository")
public class TopicWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TopicWebApplication.class, args);
	}

}
