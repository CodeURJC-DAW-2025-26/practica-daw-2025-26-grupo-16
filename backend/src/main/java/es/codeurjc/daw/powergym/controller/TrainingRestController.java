package es.codeurjc.daw.powergym.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.daw.powergym.dto.ImageDTO;
import es.codeurjc.daw.powergym.dto.ImageMapper;
import es.codeurjc.daw.powergym.dto.TrainingDTO;
import es.codeurjc.daw.powergym.dto.TrainingMapper;
import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.model.Training;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.service.ImageService;
import es.codeurjc.daw.powergym.service.TrainingService;
import es.codeurjc.daw.powergym.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/trainings")
public class TrainingRestController {

    @Autowired
	private TrainingService trainingService;

	@Autowired
	private UserService userService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private TrainingMapper trainingMapper;

	@Autowired
	private ImageMapper imageMapper;

    @GetMapping("/")
	public List<TrainingDTO> getTrainings(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size) {

		Page<Training> trainingsPage = trainingService.findPage(page, size);
		return trainingsPage.stream().map(trainingMapper::toDTO).toList();
	}

    @GetMapping("/{id}")
	public TrainingDTO getTraining(@PathVariable long id) {

		return trainingMapper.toDTO(trainingService.getTraining(id));
	}

	@PostMapping("/")
	public ResponseEntity<TrainingDTO> createTraining(@Valid @RequestBody TrainingDTO trainingDTO) {

		Training training = trainingMapper.toDomain(trainingDTO);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		User user = userService.findByEmail(username);

		training.setUser(user);

		training = trainingService.createTraining(training);

		TrainingDTO dto = trainingMapper.toDTO(training);

		URI location = fromCurrentRequest().path("/{id}")
			.buildAndExpand(dto.id()).toUri();

		return ResponseEntity.created(location).body(dto);
	}

	@PutMapping("/{id}")
	public TrainingDTO replaceTraining(@PathVariable long id, @RequestBody TrainingDTO updatedTrainingDTO) throws SQLException {

		Training updatedTraining = trainingMapper.toDomain(updatedTrainingDTO);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		User user = userService.findByEmail(username);

		updatedTraining.setUser(user);

		updatedTraining = trainingService.replaceTraining(id, updatedTraining);
		return trainingMapper.toDTO(updatedTraining);
	}

	@DeleteMapping("/{id}")
	public TrainingDTO deleteTraining(@PathVariable long id) {

		return trainingMapper.toDTO(trainingService.deleteTraining(id));
	}

	@PostMapping("/{id}/images/")
	public ResponseEntity<ImageDTO> createTrainingImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {

		if (imageFile.isEmpty()) {
			throw new IllegalArgumentException("Image file cannot be empty");
		}

		Image image = imageService.createImage(imageFile.getInputStream());
		trainingService.addImageToTraining(id, image);

		URI location = fromCurrentContextPath()
				.path("/api/images/{imageId}/media")
				.buildAndExpand(image.getId())
				.toUri();

		return ResponseEntity.created(location).body(imageMapper.toDTO(image));
	}

	@DeleteMapping("/{trainingId}/images/{imageId}")
	public ImageDTO deleteTrainingImage(@PathVariable long trainingId, @PathVariable long imageId)
			throws IOException {

		Image image = imageService.getImage(imageId);
		trainingService.removeImageFromTraining(trainingId);
		imageService.deleteImage(imageId);

		return imageMapper.toDTO(image);
	}
}