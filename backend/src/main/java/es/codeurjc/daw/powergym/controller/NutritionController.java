package es.codeurjc.daw.powergym.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.repository.UserRepository;
import es.codeurjc.daw.powergym.repository.NutritionRepository;
import es.codeurjc.daw.powergym.service.NutritionService;
import es.codeurjc.daw.powergym.service.ImageService;

@Controller
public class NutritionController {

	@Autowired
	private NutritionService nutritionService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NutritionRepository nutritionRepository;

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		if (principal != null) {

			model.addAttribute("logged", true);
			model.addAttribute("userName", principal.getName());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));

		} else {
			model.addAttribute("logged", false);
		}
	}

	@GetMapping("/nutritions")
	public String showNutritions(Model model) {

		model.addAttribute("nutritions", nutritionService.findAll());

		return "nutritions";
	}

	@GetMapping("/nutritions/{id}")
	public String showNutrition(Model model, @PathVariable long id, HttpServletRequest request) {

		Optional<Nutrition> nutrition = nutritionService.findById(id);
		if (nutrition.isPresent()) {
			model.addAttribute("nutrition", nutrition.get());

			Principal principal = request.getUserPrincipal();
			if (principal != null) {
				Optional<User> user = userRepository.findByName(principal.getName());
				if (user.isPresent()) {
					model.addAttribute("subscribed", nutrition.get().getSubscribers().contains(user.get()));
					boolean isOwner = nutrition.get().getUser() != null &&
                                	  nutrition.get().getUser().getId().equals(user.get().getId());
                	model.addAttribute("owner", isOwner);
				}
			}
			
			return "nutrition";
		} else {
			return "error";
		}

	}

	@PostMapping("/deleteNutrition/{id}")
	public String deleteNutrition(@PathVariable long id, HttpServletRequest request) {

		Optional<Nutrition> nutritionOpt = nutritionService.findById(id);
		if (nutritionOpt.isEmpty()) return "redirect:/nutritions";

		Nutrition nutrition = nutritionOpt.get();

		Principal principal = request.getUserPrincipal();
		if (principal == null) return "redirect:/login";

		User currentUser = userRepository.findByName(principal.getName()).orElseThrow();

		boolean isAdmin = request.isUserInRole("ADMIN");
		boolean isOwner = nutrition.getUser() != null &&
						nutrition.getUser().getId().equals(currentUser.getId());
		if (!isAdmin && !isOwner) {
			return "error";
		}

		nutritionService.delete(id);

		return "redirect:/nutritions";
	}

	@GetMapping("/createNutrition")
	public String createNutrition(Model model) {

		model.addAttribute("nutrition", new Nutrition());

		return "newNutrition";
	}

	@PostMapping("/createNutrition")
	public String createNutritionProcess(Model model, Nutrition nutrition,
			MultipartFile imageField,
			HttpServletRequest request) throws IOException {

		Principal principal = request.getUserPrincipal();

		if (principal != null) {
			User user = userRepository.findByName(principal.getName()).orElseThrow();
			nutrition.setUser(user);
		}

		if (!imageField.isEmpty()) {
			Image image = imageService.createImage(imageField.getInputStream());
			nutrition.setImage(image);
		}

		nutritionService.save(nutrition);

		return "redirect:/nutritions/" + nutrition.getId();
	}

	@GetMapping("/editNutrition/{id}")
	public String editNutrition(Model model, 
								@PathVariable long id, 
								HttpServletRequest request) {

		Optional<Nutrition> nutritionOpt = nutritionRepository.findById(id);

		if (nutritionOpt.isEmpty()) {
			return "redirect:/nutritions";
		}

		Nutrition nutrition = nutritionOpt.get();

		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			return "redirect:/login";
		}

		User currentUser = userRepository
				.findByName(principal.getName())
				.orElseThrow();

		boolean isAdmin = request.isUserInRole("ADMIN");
		boolean isOwner = nutrition.getUser() != null &&
				nutrition.getUser().getId().equals(currentUser.getId());

		if (!isAdmin && !isOwner) {
			return "error";
		}

		model.addAttribute("nutrition", nutrition);

		model.addAttribute("goalIsIncrease", "Increase weight".equals(nutrition.getGoal()));
		model.addAttribute("goalIsMaintain", "Maintain weight".equals(nutrition.getGoal()));
		model.addAttribute("goalIsLose", "Lose weight".equals(nutrition.getGoal()));

		return "editNutritionPage";
	}

	@PostMapping("/editNutrition")
	public String editNutritionProcess(Model model,
									Nutrition nutrition,
									boolean removeImage,
									MultipartFile imageField,
									HttpServletRequest request)
			throws IOException, SQLException {

		Optional<Nutrition> nutritionOpt = nutritionService.findById(nutrition.getId());

		if (nutritionOpt.isEmpty()) {
			return "redirect:/nutritions";
		}

		Nutrition dbNutrition = nutritionOpt.get();

		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			return "redirect:/login";
		}

		User currentUser = userRepository
				.findByName(principal.getName())
				.orElseThrow();

		boolean isAdmin = request.isUserInRole("ADMIN");
		boolean isOwner = dbNutrition.getUser() != null &&
				dbNutrition.getUser().getId().equals(currentUser.getId());

		if (!isAdmin && !isOwner) {
			return "error";
		}

		nutrition.setUser(dbNutrition.getUser());

		updateImage(nutrition, removeImage, imageField);

		nutritionService.save(nutrition);

		return "redirect:/nutritions/" + nutrition.getId();
	}

	private void updateImage(Nutrition nutrition, boolean removeImage, MultipartFile imageField)
			throws IOException, SQLException {

		if (!imageField.isEmpty()) {
			Nutrition dbNutrition = nutritionService.findById(nutrition.getId()).orElseThrow();

			if (dbNutrition.getImage() == null) {
				Image image = imageService.createImage(imageField.getInputStream());
				nutrition.setImage(image);
			} else {
				Image image = imageService.replaceImageFile(dbNutrition.getImage().getId(), imageField.getInputStream());
				nutrition.setImage(image);
			}
		} else {
			if (removeImage) {
				if (nutrition.getImage() != null) {
					imageService.deleteImage(nutrition.getImage().getId());
					nutrition.setImage(null);
				}
			} else {
				Nutrition dbNutrition = nutritionService.findById(nutrition.getId()).orElseThrow();
				nutrition.setImage(dbNutrition.getImage());
			}
		}
	}

	@PostMapping("/subscribeNutrition/{id}")
	public String subscribeNutrition(Model model, @PathVariable long id, HttpServletRequest request) {

		Optional<Nutrition> nutrition = nutritionService.findById(id);
		Principal principal = request.getUserPrincipal();

		if (nutrition.isPresent() && principal != null) {
			Optional<User> user = userRepository.findByName(principal.getName());
			if (user.isPresent()) {
				nutrition.get().getSubscribers().add(user.get());
				nutritionService.save(nutrition.get());
			}
		}

		return "redirect:/nutritions/" + id;
	}

	@PostMapping("/unsubscribeNutrition/{id}")
	public String unsubscribeNutrition(Model model, @PathVariable long id, HttpServletRequest request,
			@RequestParam(required = false) String from) {

		Optional<Nutrition> nutrition = nutritionService.findById(id);
		Principal principal = request.getUserPrincipal();

		if (nutrition.isPresent() && principal != null) {
			Optional<User> user = userRepository.findByName(principal.getName());
			if (user.isPresent()) {
				nutrition.get().getSubscribers().remove(user.get());
				nutritionService.save(nutrition.get());
			}
		}

		if ("profile".equals(from)) {
			return "redirect:/profileUser";
		} else {
			return "redirect:/nutritions/" + id;
		}
	}

}

