package com.example.demo;

import com.example.demo.some.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Test4Application {

	public static void main(String[] args) {
		SpringApplication.run(Test4Application.class, args);
	}

}