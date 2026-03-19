package es.codeurjc.daw.powergym.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.model.Training;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.repository.NutritionRepository;
import es.codeurjc.daw.powergym.repository.TrainingRepository;
import es.codeurjc.daw.powergym.repository.UserRepository;

@Service
public class DatabaseInitializer {

	@Autowired
	private TrainingRepository trainingRepository;

	@Autowired
	private NutritionRepository nutritionRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() throws IOException, URISyntaxException {

		Training training1 = new Training("Chest Plan",
				"Bench press: 4x8-10\n" + 
				"Incline dumbbell press: 4x10\n" +
				"Parallel bar dips: 3x10\n" +
				"Pulley crossovers: 4x12\n",
				"Increase weight", 60);
		setTrainingImage(training1, "/static/assets/images/chest.png");
		trainingRepository.save(training1);

		Training training2 = new Training("Arms Plan",
				"Barbell curl: 4x10\n" +
				"Hammer curl with dumbbells: 3x12\n" +
				"French press: 4x10\n" +
				"Parallel bar dips: 3x10\n",
				"Increase weight", 45);
		setTrainingImage(training2, "/static/assets/images/arm.png");
		trainingRepository.save(training2);

		Training training3 = new Training("Legs Plan",
				"Squats: 4x8-10\n" +
				"Romanian deadlift: 3x12\n" +
				"Quad extensions: 3x15\n" +
				"Calf raises: 4x15\n",
				"Increase weight", 90);
		setTrainingImage(training3, "/static/assets/images/leg.png");
		trainingRepository.save(training3);

		Training training4 = new Training("HIIT Cardio",
				"High intensity interval training for cardiovascular endurance and fat burning.",
				"Improve cardio and burn fat", 30);
		trainingRepository.save(training4);

		Training training5 = new Training("Core and Flexibility",
				"Ab isolation, core stability work, and full body stretching routine.",
				"Strengthen core and improve mobility", 45);
		trainingRepository.save(training5);

		Nutrition nutrition1 = new Nutrition("Maintenance Diet",
				"Breakfast: 2 scrambled eggs + 50 g oats\n" + 
				"Lunch: 150 g chicken + salad with oil\n" +
				"Snack: Plain Greek yoghurt with almonds\n" + 
				"Dinner: 120 g salmon + sautéed vegetables",
				"Maintain weight", 2450);
		setNutritionImage(nutrition1, "static/assets/images/maintenance-diet.png");
		nutritionRepository.save(nutrition1);

		Nutrition nutrition2 = new Nutrition("Deficit Diet",
				"Breakfast: 2 eggs + coffee\n" +
				"Lunch: Salad + 100g chicken breast\n" + 
				"Snack: Slice of wholemeal bread\n" + 
				"Dinner: Vegetables + 150g fish",
				"Lose weight", 1200);
		setNutritionImage(nutrition2, "static/assets/images/deficit-diet.png");
		nutritionRepository.save(nutrition2);

		Nutrition nutrition3 = new Nutrition("Caloric Diet",
				"Breakfast: 6 eggs + 60g oats with milk\n" + 
				"Lunch: Bowl of rice + 150g lean meat\n" + 
				"Snack: Protein shake + nuts\n" + 
				"Dinner: 150g salmon + 200g potatoes",
				"Increase weight", 3700);
		setNutritionImage(nutrition3, "static/assets/images/caloric-diet.png");
		nutritionRepository.save(nutrition3);

		Nutrition nutrition4 = new Nutrition("Strength Focus",
				"High carb, high protein diet optimized for powerlifting and strength performance.",
				"Strength gain", 2800);
		nutritionRepository.save(nutrition4);

		userRepository.save(new User("user", "user@user.com", "User", passwordEncoder.encode("user"), List.of("ROLE_USER")));
		userRepository.save(new User("admin","admin@admin.com", "Admin", passwordEncoder.encode("admin"), List.of("ROLE_USER", "ROLE_ADMIN")));
	}

	private void setTrainingImage(Training training, String classpathResource) throws IOException {
		Resource image = new ClassPathResource(classpathResource);

		Image createdImage = imageService.createImage(image.getInputStream());
		training.setImage(createdImage);
	}

	private void setNutritionImage(Nutrition nutrition, String classpathResource) throws IOException {
		Resource image = new ClassPathResource(classpathResource);

		Image createdImage = imageService.createImage(image.getInputStream());
		nutrition.setImage(createdImage);
	}
}
