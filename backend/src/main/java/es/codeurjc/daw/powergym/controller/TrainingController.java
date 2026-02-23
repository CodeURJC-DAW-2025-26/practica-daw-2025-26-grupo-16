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

import es.codeurjc.daw.powergym.model.Training;
import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.service.TrainingService;
import es.codeurjc.daw.powergym.service.ImageService;

@Controller
public class TrainingController {

	@Autowired
	private TrainingService trainingService;

	@Autowired
	private ImageService imageService;

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

	@GetMapping("/")
	public String showTrainings(Model model) {

		model.addAttribute("trainings", trainingService.findAll());

		return "trainings";
	}

	@GetMapping("/trainings/{id}")
	public String showTraining(Model model, @PathVariable long id) {

		Optional<Training> training = trainingService.findById(id);
		if (training.isPresent()) {
			model.addAttribute("training", training.get());
			return "training";
		} else {
			return "trainings";
		}

	}

	@PostMapping("/deletetraining/{id}")
	public String deleteTraining(Model model, @PathVariable long id) {

		Optional<Training> training = trainingService.findById(id);
		if (training.isPresent()) {
			trainingService.delete(id);
			model.addAttribute("training", training.get());
		}
		return "deletedtraining";
	}

	@GetMapping("/createtraining")
	public String createTraining(Model model) {

		model.addAttribute("training", new Training());

		return "newTraining";
	}

	@PostMapping("/createtraining")
	public String createTrainingProcess(Model model, Training training, MultipartFile imageField
			) throws IOException {

		if (!imageField.isEmpty()) {
			Image image = imageService.createImage(imageField.getInputStream());
			training.setImage(image);
		}

		trainingService.save(training);

		model.addAttribute("trainingId", training.getId());

		return "redirect:/trainings/" + training.getId();
	}

	@GetMapping("/edittraining/{id}")
	public String editTraining(Model model, @PathVariable long id) {

		Optional<Training> training = trainingService.findById(id);
		if (training.isPresent()) {
			model.addAttribute("training", training.get());
			return "editTrainingPage";
		} else {
			return "trainings";
		}
	}

	@PostMapping("/edittraining")
	public String editTrainingProcess(Model model, Training training, boolean removeImage, MultipartFile imageField)
			throws IOException, SQLException {

		updateImage(training, removeImage, imageField);

		trainingService.save(training);

		model.addAttribute("trainingId", training.getId());

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

}


