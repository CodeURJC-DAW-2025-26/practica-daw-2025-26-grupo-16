package es.codeurjc.daw.powergym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.security.Principal;
import java.util.Collections;
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

	@GetMapping("/profileUser")
	public String profileUser(Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}

		// valores por defecto para evitar errores en la vista Mustache
		model.addAttribute("name", principal.getName());
		model.addAttribute("email", "");

		userRepository.findByName(principal.getName()).ifPresent(user -> {
			String displayName = user.getFullName() != null && !user.getFullName().isBlank()
					? user.getFullName()
					: user.getName();
			model.addAttribute("name", displayName);
			model.addAttribute("email", user.getEmail() != null ? user.getEmail() : "");
		});

		model.addAttribute("trainings", Collections.emptyList());
		model.addAttribute("nutritions", Collections.emptyList());

		return "profileUser";
	}
}
