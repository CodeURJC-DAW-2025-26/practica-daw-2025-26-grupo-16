package es.codeurjc.daw.powergym.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import es.codeurjc.daw.powergym.security.RepositoryUserDetailsService;


@Controller
public class UserWebController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
    
	@Autowired
	private RepositoryUserDetailsService userDetailsService;

	@Autowired
	private es.codeurjc.daw.powergym.service.TrainingService trainingService;

	@Autowired
	private es.codeurjc.daw.powergym.service.NutritionService nutritionService;
	
	@GetMapping("/login")
	public String login(@RequestParam(required = false) String updated, Model model) {
		if (updated != null) {
			model.addAttribute("updated", true);
		}
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
	public String profileUser(Model model, Principal principal, @RequestParam(required = false) String saved) {
		if (principal == null) {
			return "redirect:/login";
		}

		if (saved != null) {
			model.addAttribute("saved", true);
		}

		model.addAttribute("name", principal.getName());
		model.addAttribute("email", "");

		userRepository.findByName(principal.getName()).ifPresent(user -> {
			String displayName = user.getFullName() != null && !user.getFullName().isBlank()
					? user.getFullName()
					: user.getName();
			model.addAttribute("name", displayName);
			model.addAttribute("email", user.getEmail() != null ? user.getEmail() : "");

			model.addAttribute("subscribedTrainings", trainingService.findBySubscriber(user));
			model.addAttribute("subscribedNutritions", nutritionService.findBySubscriber(user));
		});

		return "profileUser";
	}

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<User> optUser = userRepository.findByName(principal.getName());
        if (optUser.isEmpty()) {
            return "redirect:/login";
        }
        User user = optUser.get();

        // Load owned items using new owner-based service methods
        String displayName = user.getFullName() != null && !user.getFullName().isBlank()
                ? user.getFullName()
                : user.getName();
        model.addAttribute("name", displayName);
        model.addAttribute("email", user.getEmail() != null ? user.getEmail() : "");
        model.addAttribute("roles", user.getRoles());
        // Use existing profileUser template with owned items treated as subscriptions for now
        model.addAttribute("subscribedTrainings", trainingService.findByOwner(user));
        model.addAttribute("subscribedNutritions", nutritionService.findByOwner(user));
        return "profileUser";    }

	@GetMapping("/admin/users")
	public String adminUsers(Model model) {
		List<Map<String, Object>> usersView = new ArrayList<>();
		for (User user : userRepository.findAll()) {
			Map<String, Object> userView = new HashMap<>();
			userView.put("id", user.getId());
			userView.put("displayName", (user.getFullName() != null && !user.getFullName().isBlank()) ? user.getFullName() : user.getName());
			userView.put("email", user.getEmail() != null ? user.getEmail() : "");
			usersView.add(userView);
		}

		model.addAttribute("users", usersView);
		return "adminUsers";
	}

	@GetMapping("/admin/users/{id}")
	public String adminUserProfile(@org.springframework.web.bind.annotation.PathVariable long id, Model model) {
		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty()) {
			return "redirect:/admin/users";
		}

		User selectedUser = user.get();
		model.addAttribute("profile", selectedUser);
		model.addAttribute("subscribedTrainings", trainingService.findBySubscriber(selectedUser));
		model.addAttribute("subscribedNutritions", nutritionService.findBySubscriber(selectedUser));

		return "adminUserProfile";
	}

	@PostMapping("/profileUser")
	public String updateProfile(
		@RequestParam String name,
		@RequestParam String email,
		Principal principal,
		HttpServletRequest request,
		Model model) throws ServletException {

		if (principal == null) {
			return "redirect:/login";
		}

		var optUser = userRepository.findByName(principal.getName());
		if (optUser.isEmpty()) {
			return "redirect:/login";
		}

		User user = optUser.get();
		user.setFullName(name);

		boolean nameChanged = false;
		if (email != null && !email.isBlank() && !email.equals(user.getEmail())) {
			String loginName = email;
			int at = loginName.indexOf('@');
			if (at > 0) {
				loginName = loginName.substring(0, at);
			}

			if (!loginName.equals(principal.getName()) && userRepository.findByName(loginName).isPresent()) {
				model.addAttribute("error", "The username derived from the new email is already in use");
				return profileUser(model, principal, null);
			}

			user.setEmail(email);
			user.setName(loginName);
			nameChanged = !loginName.equals(principal.getName());
		}

		userRepository.save(user);

		if (nameChanged) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					userDetails, userDetails.getPassword(), userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		return "redirect:/profileUser?saved";
	}
}
