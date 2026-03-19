
package es.codeurjc.daw.powergym.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
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

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/nutritions")
public class NutritionRestController {

	@Autowired
	private NutritionService nutritionService;

	@Autowired
	private UserService userRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private NutritionMapper nutritionMapper;

	@Autowired
	private ImageMapper imageMapper;

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
			@RequestParam(defaultValue = "6") int size) {

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

		return nutritionMapper.toDTO(nutritionService.getNutrition(id));
	}

	@PostMapping("/")
	public ResponseEntity<NutritionDTO> createNutrition(@Valid @RequestBody NutritionDTO nutritionDTO) {

		Nutrition nutrition = nutritionMapper.toDomain(nutritionDTO);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();

		User user = userRepository.findByName(username);

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

		User user = userRepository.findByName(username);

		updatedNutrition.setUser(user);

		updatedNutrition = nutritionService.replaceNutrition(id, updatedNutrition);
		return nutritionMapper.toDTO(updatedNutrition);
	}

	@DeleteMapping("/{id}")
	public NutritionDTO deleteNutrition(@PathVariable long id) {

		return nutritionMapper.toDTO(nutritionService.deleteNutrition(id));
	}

	@PostMapping("/{id}/images/")
	public ResponseEntity<ImageDTO> createNutritionImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {

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

	@DeleteMapping("/{nutritionId}/images/{imageId}")
	public ImageDTO deleteNutritionImage(@PathVariable long nutritionId, @PathVariable long imageId)
			throws IOException {

		Image image = imageService.getImage(imageId);
		nutritionService.removeImageFromNutrition(nutritionId);
		imageService.deleteImage(imageId);

		return imageMapper.toDTO(image);
	}
}
