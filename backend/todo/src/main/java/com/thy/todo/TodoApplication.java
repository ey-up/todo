package com.thy.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	// todo use http only cookie for jwt
	// todo add role
	// todo metrik için grafana
	// todo logları elastic'e bas
	// todo resillinj4j
	// todo connector
	// todo deployment
}
