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
import es.codeurjc.daw.powergym.service.PdfExportService;
import es.codeurjc.daw.powergym.service.TrainingService;
import es.codeurjc.daw.powergym.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/trainings")
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

	@Autowired
	private PdfExportService pdfExportService;

    @GetMapping("/")
	public List<TrainingDTO> getTrainings(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<Training> trainingsPage = trainingService.findPage(page, size);
		return trainingsPage.stream().map(trainingMapper::toDTO).toList();
	}

    @GetMapping("/{id}")
	public TrainingDTO getTraining(@PathVariable long id) {

		Training training = trainingService.getTraining(id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		boolean subscribed = false;

		if (auth != null && auth.isAuthenticated()
				&& !"anonymousUser".equals(auth.getPrincipal())) {

			User user = userService.findByEmail(auth.getName());

			if (user != null) {
				subscribed = training.getSubscribers()
						.stream()
						.anyMatch(u -> u.getId().equals(user.getId()));
			}
		}

		return new TrainingDTO(
				training.getId(),
				training.getName(),
				training.getDescription(),
				training.getGoal(),
				training.getTime(),
				training.getImage() != null ? imageMapper.toDTO(training.getImage()) : null,
				training.getUser() != null ? training.getUser().getId() : null,
				subscribed
		);
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

	@PostMapping("/{id}/subscribe")
	public TrainingDTO subscribeTraining(@PathVariable long id) {

		Training training = trainingService.getTraining(id);

		User user = userService.findByEmail(
			SecurityContextHolder.getContext().getAuthentication().getName()
		);

		training.getSubscribers().add(user);
		trainingService.save(training);

		return getTraining(id);
	}

	@DeleteMapping("/{id}/subscribe")
	public TrainingDTO unsubscribeTraining(@PathVariable long id) {

		Training training = trainingService.getTraining(id);

		User user = userService.findByEmail(
			SecurityContextHolder.getContext().getAuthentication().getName()
		);

		training.getSubscribers().removeIf(u -> u.getId().equals(user.getId()));
		trainingService.save(training);

		return getTraining(id);
	}

	@PostMapping("/{id}/images/")
	public ResponseEntity<ImageDTO> createTrainingImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {

		Training training = trainingService.getTraining(id);

		trainingService.checkOwnerOrAdmin(training);

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

	@DeleteMapping("/{trainingId}/images/")
	public ImageDTO deleteTrainingImage(@PathVariable long trainingId) throws IOException {

		Training training = trainingService.getTraining(trainingId);

		trainingService.checkOwnerOrAdmin(training);

		Image image = training.getImage();

		if(image == null){
			throw new IllegalArgumentException("No image associated with this training");
		}

		trainingService.removeImageFromTraining(trainingId);

		imageService.deleteImage(image.getId());

		return imageMapper.toDTO(image);
	}

	@PutMapping("/{id}/images")
	public ResponseEntity<ImageDTO> replaceTrainingImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

		Training training = trainingService.getTraining(id);

		trainingService.checkOwnerOrAdmin(training);

		if (training.getImage() != null) {
			imageService.replaceImageFile(training.getImage().getId(), imageFile.getInputStream());
			return ResponseEntity.ok(imageMapper.toDTO(training.getImage()));
		} else {
			Image newImage = imageService.createImage(imageFile.getInputStream());
			trainingService.addImageToTraining(id, newImage);
			return ResponseEntity.ok(imageMapper.toDTO(newImage));
		}
	}

	@GetMapping("/{id}/pdf")
	public ResponseEntity<byte[]> downloadTrainingPdf(@PathVariable long id) {

		Training training = trainingService.getTraining(id);

		byte[] pdf = pdfExportService.buildTrainingPdf(training);

		String fileName = "training-" + id + ".pdf";

		return ResponseEntity.ok()
				.contentType(org.springframework.http.MediaType.APPLICATION_PDF)
				.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + fileName + "\"")
				.body(pdf);
	}
}