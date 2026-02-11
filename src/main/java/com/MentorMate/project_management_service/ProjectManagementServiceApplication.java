package com.MentorMate.project_management_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ProjectManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementServiceApplication.class, args);
	}

}
