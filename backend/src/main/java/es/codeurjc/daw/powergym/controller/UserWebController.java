package es.codeurjc.daw.powergym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserWebController {
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/loginerror")
	public String loginerror() {
		return "loginerror";
	}

	@GetMapping("/register")
    public String register(Model model) {
        return "register";
    }
}
