package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Login {
	@Autowired
	private EmployeeRepo repo;
	  
	public Employee login(String Username, String Password) {
		  Employee user = repo.findByUsernameAndPassword(Username, Password);
		   return user;
		  }
}
