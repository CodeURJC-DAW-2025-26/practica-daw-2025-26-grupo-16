package es.codeurjc.daw.powergym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.repository.UserRepository;


@Controller
public class UserWebController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
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

	@PostMapping("/register")
	public String doRegister(
		@RequestParam String fullName,
		@RequestParam String username,        
		@RequestParam String password,
		@RequestParam String confirmPassword,
		Model model) {

		if (!password.equals(confirmPassword)) {
			model.addAttribute("error", "Passwords do not match");
			return "register";
		}

		String loginName = username;
		int at = loginName.indexOf('@');
		if (at > 0) {
			loginName = loginName.substring(0, at);
		}

		if (userRepository.findByName(loginName).isPresent()) {
			model.addAttribute("error", "User already exists");
			return "register";
		}

		String encoded = passwordEncoder.encode(password);
		User user = new User(loginName, username, fullName, encoded, List.of("ROLE_USER"));
		userRepository.save(user);

		return "redirect:/login";
	}
}
