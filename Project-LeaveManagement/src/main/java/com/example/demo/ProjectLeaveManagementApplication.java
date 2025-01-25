package com.example.demo;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectLeaveManagementApplication {

	public static void main(String[] args) {
		new File(EmployeeController.uploadDirectory).mkdir();
		SpringApplication.run(ProjectLeaveManagementApplication.class, args);
	}

}
