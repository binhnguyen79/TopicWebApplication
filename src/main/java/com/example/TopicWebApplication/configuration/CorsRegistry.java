package com.example.TopicWebApplication.configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableWebSecurity
public class CorsRegistry implements WebMvcConfigurer{
	@Override
	public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
		registry.addMapping("/api/**")
				.allowedMethods("POST", "PUT", "GET", "OPTIONS", "DELETE")
				.allowedOrigins("*");
	}
}
