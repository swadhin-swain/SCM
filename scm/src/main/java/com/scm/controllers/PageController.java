package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessegeType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class PageController {


	@Autowired
	private UserService userService;


	@GetMapping("/")
	public String index() {

		return "redirect:/home";
	}

	@GetMapping("/home")
	public String home(Model model) {
		
		System.out.println("Home page handler");
		
		model.addAttribute("name", "Swadhin Swain");
		model.addAttribute("youtube", "swadhin Youtube");
		
		return "home";
	}

	// about

	@GetMapping("/about")
	public String aboutPage() {
		System.out.println("about page loading");
		return "about";
	}
	

	//services

	@GetMapping("/service")
	public String servicesPage() {
		System.out.println("service page loading");
		return "service";
	}


	//contact
	@GetMapping("/contact")
	public String contact () {

		System.out.println("contact page");
		return "contact";
	}
 
	// login

	@GetMapping("/login")
	public String login() {

		System.out.println("login page");
		return "login";
	}

	// signup

	@GetMapping("/register")
	public String register(Model model) {
		//send the empty object to collect data
		UserForm userForm = new UserForm();
	
		// default data bhi dall sakte hai
		model.addAttribute("userForm", userForm);


		System.out.println("register page");
		return "register";
	}

	// processing register form

	@PostMapping("/do-register")
	public String processRegister(@Valid@ModelAttribute UserForm userForm/*this attribute can hold the 
	value of the userForn object*/,BindingResult result,HttpSession session
	) {
		System.out.println("processing register");
		// fetch the form data
		System.out.println(userForm);

		// TODO::validate userForm data
		if (result.hasErrors()) {
			return "register";
		}


		// save to database

		// user service
		

		User user = new User();
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());
		user.setAbout(userForm.getAbout());
		user.setPhoneNumber(userForm.getPhoneNumber());
		user.setProfilePic("https://images.app.goo.gl/hjinRWNwJMCQFjDB6");

		User savedUser= userService.saveUser(user);
		System.out.println(savedUser);
		// message = "registration successful"

		// add the message
		Message message = Message.builder().
		content("Registration successful").type(MessegeType.green
		).build();

		session.setAttribute("message", message);
		
		// redirect to any page
		return "redirect:/register";
	}
	

}
