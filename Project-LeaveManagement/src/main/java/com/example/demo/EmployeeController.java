package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EmployeeController {
	@Autowired
	EmployeeRepo repo;
	
	@Autowired 
	Login l;
	@Autowired
	private JavaMailSender js;
	
    @Autowired
    EmployeeRepo repo1;
	
	@RequestMapping("/show")
	public String show(Model model) {
		List<Employee> emp=repo.findAll();
		model.addAttribute("emp", emp);
		return "index.jsp";
	}
	
	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/imagedata";
	
	@PostMapping("/add")
	public String add(Employee em ,@RequestParam("img") MultipartFile file) {
		String filename = em.getUsername()+file.getOriginalFilename().substring(file.getOriginalFilename().length()-4);
		Path fileNameAndPath =Paths.get(uploadDirectory,filename);
		try {
			Files.write(fileNameAndPath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		em.setPic(filename);
		em.setPassword(em.getDepartment()+em.getUsername()+em.getProject());
		repo.save(em);
		SimpleMailMessage message= new SimpleMailMessage();
		message.setTo(em.getEmail());
		message.setSubject("Your Credentials for Timesheet Application");
		message.setText("Employee ID = "+em.getUsername()+", Password = "+em.getPassword());
		
		js.send(message);
		System.out.println("Mail sent");
		return "redirect:/show";
		
   }
	
	@RequestMapping("/delete")
	public String delete(@RequestParam("userId") String username) {
		repo.deleteById(username);
		return "redirect:/show";
	}
	
	@RequestMapping("/update")
	public String update(@RequestParam("userId") String username,Model model) {
        Employee emp1 = repo.findById(username).orElse(null);
		model.addAttribute("emp1", emp1 );
		return "update.jsp";
	}
	
	@RequestMapping("/home")
	public String home() {
		return "login.jsp";
	}
	
	@RequestMapping("/login")
	public String login(@ModelAttribute Employee emp2,Model model) {
		String Username = emp2.getUsername();
		String Password =emp2.getPassword();
		if(Username.equalsIgnoreCase("HRLogin") & Password.equalsIgnoreCase("HR123")) {
			return "redirect:/show";
		}
		else {
			Employee user = l.login(Username, Password);
			if(Objects.nonNull(user)) {
				Employee emp3 = repo.findById(Username).orElse(null);
				model.addAttribute("emp3", emp3 );
				return "user.jsp";
			}
			else
				return "login.jsp";
        }
	}
	
	@RequestMapping("/leave")
	public String leave(Employee u,Model model) {
		repo1.save(u);
		return "user.jsp";
	}
	@RequestMapping("/leave1")
	public String leave1(Employee u,Model model) {
		repo1.save(u);
		return "redirect:/check";
	}
	
	@RequestMapping("/check")
	public String check(Model model) {
		List<Employee> use=repo1.findAll();
		model.addAttribute("us", use);
		return "leave.jsp";
	}
	@RequestMapping("/status")
	public String status(@RequestParam("username") int id,Model model) {
        Employee emp4 = repo1.findById(id).orElse(null);
		model.addAttribute("emp4", emp4 );
		System.out.println(emp4);
		return "status.jsp";
	}
}