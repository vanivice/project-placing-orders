package com.attestation.project.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProjectPlacingOrders {

	public static void main(String[] args) {
		SpringApplication.run(ProjectPlacingOrders.class, args);
	}

}
