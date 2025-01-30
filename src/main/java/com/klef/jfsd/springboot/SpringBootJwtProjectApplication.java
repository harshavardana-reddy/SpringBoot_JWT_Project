package com.klef.jfsd.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SpringBootJwtProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJwtProjectApplication.class, args);
		System.out.println("Spring Boot JWT Project is Running .......!");
	}

}
