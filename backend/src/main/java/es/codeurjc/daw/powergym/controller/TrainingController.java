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

import es.codeurjc.daw.powergym.model.Training;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.repository.UserRepository;
import es.codeurjc.daw.powergym.repository.TrainingRepository;
import es.codeurjc.daw.powergym.service.TrainingService;
import es.codeurjc.daw.powergym.service.ImageService;

@Controller
public class TrainingController {

	@Autowired
	private TrainingService trainingService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TrainingRepository trainingRepository;

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

	@GetMapping("/trainings")
	public String showTrainings(Model model) {

		model.addAttribute("trainings", trainingService.findAll());

		return "trainings";
	}

	@GetMapping("/trainings/{id}")
	public String showTraining(Model model, @PathVariable long id, HttpServletRequest request) {

		Optional<Training> training = trainingService.findById(id);
		if (training.isPresent()) {
			model.addAttribute("training", training.get());

			Principal principal = request.getUserPrincipal();
			if (principal != null) {
				Optional<User> user = userRepository.findByName(principal.getName());
				if (user.isPresent()) {
					model.addAttribute("subscribed", training.get().getSubscribers().contains(user.get()));
					boolean isOwner = training.get().getUser() != null &&
                                	  training.get().getUser().getId().equals(user.get().getId());
                	model.addAttribute("owner", isOwner);
				}
			}
			
			return "training";
		} else {
			return "error";
		}

	}

	@PostMapping("/deleteTraining/{id}")
	public String deleteTraining(@PathVariable long id, HttpServletRequest request) {

		Optional<Training> trainingOpt = trainingService.findById(id);
		if (trainingOpt.isEmpty()) return "redirect:/trainings";

		Training training = trainingOpt.get();

		Principal principal = request.getUserPrincipal();
		if (principal == null) return "redirect:/login";

		User currentUser = userRepository.findByName(principal.getName()).orElseThrow();

		boolean isAdmin = request.isUserInRole("ADMIN");
		boolean isOwner = training.getUser() != null &&
						training.getUser().getId().equals(currentUser.getId());

		if (!isAdmin && !isOwner) {
			return "error"; 
		}

		trainingService.delete(id);

		return "redirect:/trainings";
	}

	@GetMapping("/createTraining")
	public String createTraining(Model model) {

		model.addAttribute("training", new Training());

		return "newTraining";
	}

	@PostMapping("/createTraining")
	public String createTrainingProcess(Model model, Training training,
			MultipartFile imageField,
			HttpServletRequest request) throws IOException {

		Principal principal = request.getUserPrincipal();

		if (principal != null) {
			User user = userRepository.findByName(principal.getName()).orElseThrow();
			training.setUser(user);
		}

		if (!imageField.isEmpty()) {
			Image image = imageService.createImage(imageField.getInputStream());
			training.setImage(image);
		}

		trainingService.save(training);

		return "redirect:/trainings/" + training.getId();
	}

	@GetMapping("/editTraining/{id}")
	public String editTraining(Model model, 
								@PathVariable long id, 
								HttpServletRequest request) {

		Optional<Training> trainingOpt = trainingRepository.findById(id);

		if (trainingOpt.isEmpty()) {
			return "redirect:/trainings";
		}

		Training training = trainingOpt.get();

		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			return "redirect:/login";
		}

		User currentUser = userRepository
				.findByName(principal.getName())
				.orElseThrow();

		boolean isAdmin = request.isUserInRole("ADMIN");
		boolean isOwner = training.getUser() != null &&
				training.getUser().getId().equals(currentUser.getId());

		if (!isAdmin && !isOwner) {
			return "error";
		}

		model.addAttribute("training", training);

		model.addAttribute("goalIsIncrease", "Increase weight".equals(training.getGoal()));
		model.addAttribute("goalIsMaintain", "Maintain weight".equals(training.getGoal()));
		model.addAttribute("goalIsLose", "Lose weight".equals(training.getGoal()));

		return "editTrainingPage";
	}

	@PostMapping("/editTraining")
	public String editTrainingProcess(Model model,
									Training training,
									boolean removeImage,
									MultipartFile imageField,
									HttpServletRequest request)
			throws IOException, SQLException {

		Optional<Training> trainingOpt = trainingService.findById(training.getId());

		if (trainingOpt.isEmpty()) {
			return "redirect:/trainings";
		}

		Training dbTraining = trainingOpt.get();

		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			return "redirect:/login";
		}

		User currentUser = userRepository
				.findByName(principal.getName())
				.orElseThrow();

		boolean isAdmin = request.isUserInRole("ADMIN");
		boolean isOwner = dbTraining.getUser() != null &&
				dbTraining.getUser().getId().equals(currentUser.getId());

		if (!isAdmin && !isOwner) {
			return "error";
		}

		training.setUser(dbTraining.getUser());

		updateImage(training, removeImage, imageField);

		trainingService.save(training);

		return "redirect:/trainings/" + training.getId();
	}

	private void updateImage(Training training, boolean removeImage, MultipartFile imageField)
			throws IOException, SQLException {

		if (!imageField.isEmpty()) {
			Training dbTraining = trainingService.findById(training.getId()).orElseThrow();

			if (dbTraining.getImage() == null) {
				Image image = imageService.createImage(imageField.getInputStream());
				training.setImage(image);
			} else {
				Image image = imageService.replaceImageFile(dbTraining.getImage().getId(), imageField.getInputStream());
				training.setImage(image);
			}
		} else {
			if (removeImage) {
				if (training.getImage() != null) {
					imageService.deleteImage(training.getImage().getId());
					training.setImage(null);
				}
			} else {
				Training dbTraining = trainingService.findById(training.getId()).orElseThrow();
				training.setImage(dbTraining.getImage());
			}
		}
	}

	@PostMapping("/subscribeTraining/{id}")
	public String subscribeTraining(Model model, @PathVariable long id, HttpServletRequest request) {

		Optional<Training> training = trainingService.findById(id);
		Principal principal = request.getUserPrincipal();

		if (training.isPresent() && principal != null) {
			Optional<User> user = userRepository.findByName(principal.getName());
			if (user.isPresent()) {
				training.get().getSubscribers().add(user.get());
				trainingService.save(training.get());
			}
		}

		return "redirect:/trainings/" + id;
	}

	@PostMapping("/unsubscribeTraining/{id}")
	public String unsubscribeTraining(Model model, @PathVariable long id, HttpServletRequest request,
			@RequestParam(required = false) String from) {

		Optional<Training> training = trainingService.findById(id);
		Principal principal = request.getUserPrincipal();

		if (training.isPresent() && principal != null) {
			Optional<User> user = userRepository.findByName(principal.getName());
			if (user.isPresent()) {
				training.get().getSubscribers().remove(user.get());
				trainingService.save(training.get());
			}
		}

		if ("profile".equals(from)) {
			return "redirect:/profileUser";
		} else {
			return "redirect:/trainings/" + id;
		}
	}

}


