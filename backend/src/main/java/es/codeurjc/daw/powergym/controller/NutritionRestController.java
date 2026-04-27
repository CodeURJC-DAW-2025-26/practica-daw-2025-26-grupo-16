
package es.codeurjc.daw.powergym.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import es.codeurjc.daw.powergym.dto.NutritionDTO;
import es.codeurjc.daw.powergym.dto.NutritionMapper;
import es.codeurjc.daw.powergym.dto.ImageDTO;
import es.codeurjc.daw.powergym.dto.ImageMapper;
import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.service.NutritionService;
import es.codeurjc.daw.powergym.service.UserService;
import es.codeurjc.daw.powergym.service.ImageService;
import es.codeurjc.daw.powergym.service.PdfExportService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/v1/nutritions")
public class NutritionRestController {

	@Autowired
	private NutritionService nutritionService;

	@Autowired
	private UserService userService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private NutritionMapper nutritionMapper;

	@Autowired
	private ImageMapper imageMapper;

	@Autowired
	private PdfExportService pdfExportService;

	@Operation(summary = "Get all nutritions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all nutritions", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Nutrition.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid parametes", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nutritions not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

	@GetMapping("/")
	public List<NutritionDTO> getNutritions(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<Nutrition> nutritionsPage = nutritionService.findPage(page, size);
		return nutritionsPage.stream().map(nutritionMapper::toDTO).toList();
	}

	@Operation(summary = "Get a nutrition by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nutrition found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = NutritionDTO.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request - Invalid parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized access - Authentication is required", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden - You don't have permission to access", content = @Content),
        @ApiResponse(responseCode = "404", description = "Nutrition not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

	@GetMapping("/{id}")
	public NutritionDTO getNutrition(@PathVariable long id) {

		Nutrition nutrition = nutritionService.getNutrition(id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		boolean subscribed = false;

		if (auth != null && auth.isAuthenticated()
				&& !"anonymousUser".equals(auth.getPrincipal())) {

			User user = userService.findByEmail(auth.getName());

			if (user != null) {
				subscribed = nutrition.getSubscribers()
						.stream()
						.anyMatch(u -> u.getId().equals(user.getId()));
			}
		}

		return new NutritionDTO(
				nutrition.getId(),
				nutrition.getName(),
				nutrition.getDescription(),
				nutrition.getGoal(),
				nutrition.getCalories(),
				nutrition.getImage() != null ? imageMapper.toDTO(nutrition.getImage()) : null,
				nutrition.getUser() != null ? nutrition.getUser().getId() : null,
				subscribed
		);
	}

	@PostMapping("/")
	public ResponseEntity<NutritionDTO> createNutrition(@Valid @RequestBody NutritionDTO nutritionDTO) {

		Nutrition nutrition = nutritionMapper.toDomain(nutritionDTO);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		System.out.println("Authenticated username: " + username);

		User user = userService.findByEmail(username);

		nutrition.setUser(user);

		nutrition = nutritionService.createNutrition(nutrition);

		NutritionDTO dto = nutritionMapper.toDTO(nutrition);

		URI location = fromCurrentRequest().path("/{id}")
			.buildAndExpand(dto.id()).toUri();

		return ResponseEntity.created(location).body(dto);
	}

	@PutMapping("/{id}")
	public NutritionDTO replaceNutrition(@PathVariable long id, @RequestBody NutritionDTO updatedNutritionDTO) throws SQLException {

		Nutrition updatedNutrition = nutritionMapper.toDomain(updatedNutritionDTO);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		User user = userService.findByEmail(username);

		updatedNutrition.setUser(user);

		updatedNutrition = nutritionService.replaceNutrition(id, updatedNutrition);
		return nutritionMapper.toDTO(updatedNutrition);
	}

	@DeleteMapping("/{id}")
	public NutritionDTO deleteNutrition(@PathVariable long id) {

		return nutritionMapper.toDTO(nutritionService.deleteNutrition(id));
	}

	@PostMapping("/{id}/subscribe")
	public NutritionDTO subscribeNutrition(@PathVariable long id) {

		Nutrition nutrition = nutritionService.getNutrition(id);

		User user = userService.findByEmail(
			SecurityContextHolder.getContext().getAuthentication().getName()
		);

		nutrition.getSubscribers().add(user);
		nutritionService.save(nutrition);

		return getNutrition(id);
	}

	@DeleteMapping("/{id}/subscribe")
	public NutritionDTO unsubscribeNutrition(@PathVariable long id) {

		Nutrition nutrition = nutritionService.getNutrition(id);

		User user = userService.findByEmail(
			SecurityContextHolder.getContext().getAuthentication().getName()
		);

		nutrition.getSubscribers().removeIf(u -> u.getId().equals(user.getId()));
		nutritionService.save(nutrition);

		return getNutrition(id);
	}

	@PostMapping("/{id}/images/")
	public ResponseEntity<ImageDTO> createNutritionImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {

		Nutrition nutrition = nutritionService.getNutrition(id);

		nutritionService.checkOwnerOrAdmin(nutrition);

		if (imageFile.isEmpty()) {
			throw new IllegalArgumentException("Image file cannot be empty");
		}

		Image image = imageService.createImage(imageFile.getInputStream());
		nutritionService.addImageToNutrition(id, image);

		URI location = fromCurrentContextPath()
				.path("/api/images/{imageId}/media")
				.buildAndExpand(image.getId())
				.toUri();

		return ResponseEntity.created(location).body(imageMapper.toDTO(image));
	}

	@DeleteMapping("/{nutritionId}/images/")
	public ImageDTO deleteNutritionImage(@PathVariable long nutritionId) throws IOException {

		Nutrition nutrition = nutritionService.getNutrition(nutritionId);

		nutritionService.checkOwnerOrAdmin(nutrition);

		Image image = nutrition.getImage();

		if(image == null){
			throw new IllegalArgumentException("No image associated with this nutrition");
		}

		nutritionService.removeImageFromNutrition(nutritionId);

		imageService.deleteImage(image.getId());

		return imageMapper.toDTO(image);
	}

	@PutMapping("/{id}/images")
	public ResponseEntity<ImageDTO> replaceNutritionImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

		Nutrition nutrition = nutritionService.getNutrition(id);

		nutritionService.checkOwnerOrAdmin(nutrition);

		if (nutrition.getImage() != null) {
			imageService.replaceImageFile(nutrition.getImage().getId(), imageFile.getInputStream());
			return ResponseEntity.ok(imageMapper.toDTO(nutrition.getImage()));
		} else {
			Image newImage = imageService.createImage(imageFile.getInputStream());
			nutritionService.addImageToNutrition(id, newImage);
			return ResponseEntity.ok(imageMapper.toDTO(newImage));
		}
	}

	@GetMapping("/{id}/pdf")
	public ResponseEntity<byte[]> downloadNutritionPdf(@PathVariable long id) {

		Nutrition nutrition = nutritionService.getNutrition(id); 

		byte[] pdf = pdfExportService.buildNutritionPdf(nutrition);

		String fileName = "nutrition-" + id + ".pdf";

		return ResponseEntity.ok()
				.contentType(org.springframework.http.MediaType.APPLICATION_PDF)
				.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + fileName + "\"")
				.body(pdf);
	}

	@GetMapping("/subscribed")
	public List<NutritionDTO> getSubscribedNutritions() {
		User user = userService.findByEmail(
			SecurityContextHolder.getContext().getAuthentication().getName()
		);

		List<Nutrition> nutritions = nutritionService.findBySubscriber(user);

		return nutritions.stream().map(nutritionMapper::toDTO).toList();
	}
}
