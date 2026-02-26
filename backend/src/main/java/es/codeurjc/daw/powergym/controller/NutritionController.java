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
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.repository.UserRepository;
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
				}
			}
			
			return "nutrition";
		} else {
			return "nutritions";
		}

	}

	@PostMapping("/deleteNutrition/{id}")
	public String deleteNutrition(Model model, @PathVariable long id) {

		Optional<Nutrition> nutrition = nutritionService.findById(id);
		if (nutrition.isPresent()) {
			nutritionService.delete(id);
			model.addAttribute("nutrition", nutrition.get());
		}
		return "deletednutrition";
	}

	@GetMapping("/createNutrition")
	public String createNutrition(Model model) {

		model.addAttribute("nutrition", new Nutrition());

		return "newNutrition";
	}

	@PostMapping("/createNutrition")
	public String createNutritionProcess(Model model, Nutrition nutrition, MultipartFile imageField
			) throws IOException {

		if (!imageField.isEmpty()) {
			Image image = imageService.createImage(imageField.getInputStream());
			nutrition.setImage(image);
		}

		nutritionService.save(nutrition);

		model.addAttribute("nutritionId", nutrition.getId());

		return "redirect:/nutritions/" + nutrition.getId();
	}

	@GetMapping("/editNutrition/{id}")
	public String editNutrition(Model model, @PathVariable long id) {

		Optional<Nutrition> nutrition = nutritionService.findById(id);
		if (nutrition.isPresent()) {
			Nutrition n = nutrition.get();
			model.addAttribute("nutrition", n);

			model.addAttribute("goalIsIncrease", "Increase weight".equals(n.getGoal()));
			model.addAttribute("goalIsMaintain", "Maintain weight".equals(n.getGoal()));
			model.addAttribute("goalIsLose", "Lose weight".equals(n.getGoal()));
			
			return "editNutritionPage";
		} else {
			return "nutritions";
		}
	}

	@PostMapping("/editNutrition")
	public String editNutritionProcess(Model model, Nutrition nutrition, boolean removeImage, MultipartFile imageField)
			throws IOException, SQLException {

		updateImage(nutrition, removeImage, imageField);

		nutritionService.save(nutrition);

		model.addAttribute("nutritionId", nutrition.getId());

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
	public String unsubscribeNutrition(Model model, @PathVariable long id, HttpServletRequest request) {

		Optional<Nutrition> nutrition = nutritionService.findById(id);
		Principal principal = request.getUserPrincipal();

		if (nutrition.isPresent() && principal != null) {
			Optional<User> user = userRepository.findByName(principal.getName());
			if (user.isPresent()) {
				nutrition.get().getSubscribers().remove(user.get());
				nutritionService.save(nutrition.get());
			}
		}

		return "redirect:/nutritions/" + id;
	}

}

