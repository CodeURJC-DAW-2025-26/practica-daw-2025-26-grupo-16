package es.codeurjc.daw.powergym.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.repository.NutritionRepository;

@Service
public class NutritionService {

	@Autowired
	private NutritionRepository nutritionRepository;

	@Autowired
	private UserService userRepository;

	public Optional<Nutrition> findById(long id) {
		return nutritionRepository.findById(id);
	}
	
	public boolean exist(long id) {
		return nutritionRepository.existsById(id);
	}

	public List<Nutrition> findAll() {
		return nutritionRepository.findAll();
	}

	public Page<Nutrition> findPage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		return nutritionRepository.findAll(pageable);
	}

	public void save(Nutrition nutrition) {
		nutritionRepository.save(nutrition);
	}

	public void delete(long id) {
		nutritionRepository.deleteById(id);
	}

    public List<Nutrition> findBySubscriber(User user) {
        return nutritionRepository.findBySubscribersContains(user);
    }

    // Owner-based retrieval: nutritions owned by a user
    public List<Nutrition> findByOwner(User user) {
        return nutritionRepository.findByUser(user);
    }

	public Optional<Nutrition> findByIdWithUser(long id) {
		return nutritionRepository.findWithUserById(id);
	}

	public Collection<Nutrition> getNutritions() {

		return nutritionRepository.findAll();
	}

	public Nutrition getNutrition(long id) {

		return nutritionRepository.findById(id).orElseThrow();
	}

	public Nutrition createNutrition(Nutrition nutrition) {

		if (nutrition.getId() != null) {
			throw new IllegalArgumentException();
		}

		Long userId = nutrition.getUser().getId();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));

		nutrition.setUser(user);

		return nutritionRepository.save(nutrition);
	}

	public Nutrition replaceNutrition(long id, Nutrition updatedNutrition) throws SQLException {

		Nutrition oldNutrition = nutritionRepository.findById(id).orElseThrow();
		updatedNutrition.setId(id);

		if (oldNutrition.getImage() != null) {
			// Transfer the image from the old nutrition to the new one
			updatedNutrition.setImage(oldNutrition.getImage());
		}

		nutritionRepository.save(updatedNutrition);

		return updatedNutrition;
	}

	public Nutrition deleteNutrition(long id) {

		Nutrition nutrition = nutritionRepository.findById(id).orElseThrow();

		nutritionRepository.deleteById(id);

		return nutrition;
	}

	public Nutrition addImageToNutrition(long id, Image image) {
		Nutrition nutrition = nutritionRepository.findById(id).orElseThrow();
		nutrition.setImage(image);
		nutritionRepository.save(nutrition);

		return nutrition;
	}

	public Nutrition removeImageFromNutrition(long nutritionId) {
		Nutrition nutrition = nutritionRepository.findById(nutritionId).orElseThrow();
		nutrition.setImage(null);
		nutritionRepository.save(nutrition);

		return nutrition;
	}
}
