
package es.codeurjc.daw.powergym.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import es.codeurjc.daw.powergym.dto.NutritionDTO;
import es.codeurjc.daw.powergym.dto.NutritionMapper;
import es.codeurjc.daw.powergym.dto.ImageDTO;
import es.codeurjc.daw.powergym.dto.ImageMapper;
import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.service.NutritionService;
import es.codeurjc.daw.powergym.service.ImageService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/nutritions")
public class NutritionRestController {

	@Autowired
	private NutritionService nutritionService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private NutritionMapper nutritionMapper;

	@Autowired
	private ImageMapper imageMapper;

	@GetMapping("/")
	public Collection<NutritionDTO> getNutritions() {

		return nutritionMapper.toDTOs(nutritionService.getNutritions());
	}

	@GetMapping("/{id}")
	public NutritionDTO getNutrition(@PathVariable long id) {

		return nutritionMapper.toDTO(nutritionService.getNutrition(id));
	}

	@PostMapping("/")
	public ResponseEntity<NutritionDTO> createNutrition(@RequestBody NutritionDTO nutritionDTO) {

		Nutrition nutrition = nutritionMapper.toDomain(nutritionDTO);
		nutrition = nutritionService.createNutrition(nutrition);
		nutritionDTO = nutritionMapper.toDTO(nutrition);

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(nutritionDTO.id()).toUri();

		return ResponseEntity.created(location).body(nutritionDTO);
	}

	@PutMapping("/{id}")
	public NutritionDTO replaceNutrition(@PathVariable long id, @RequestBody NutritionDTO updatedNutritionDTO) throws SQLException {

		Nutrition updatedNutrition = nutritionMapper.toDomain(updatedNutritionDTO);
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
