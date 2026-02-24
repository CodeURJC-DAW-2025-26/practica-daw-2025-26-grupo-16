package es.codeurjc.daw.powergym.service;

import java.io.IOException;
import java.net.URISyntaxException;

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

		Training training1 = new Training("Push Day",
				"Full upper body push workout including bench press, incline press, and shoulder isolation exercises.",
				"Build chest and shoulder strength", 60);
		trainingRepository.save(training1);

		Training training2 = new Training("Pull Day",
				"Back and biceps focused workout with deadlifts, rows, pull-ups, and bicep curls.",
				"Develop back thickness and arm size", 60);
		trainingRepository.save(training2);

		Training training3 = new Training("Leg Day",
				"Lower body strength and hypertrophy with squats, leg press, leg curls, and calf raises.",
				"Build leg strength and size", 75);
		trainingRepository.save(training3);

		Training training4 = new Training("HIIT Cardio",
				"High intensity interval training for cardiovascular endurance and fat burning.",
				"Improve cardio and burn fat", 30);
		trainingRepository.save(training4);

		Training training5 = new Training("Core and Flexibility",
				"Ab isolation, core stability work, and full body stretching routine.",
				"Strengthen core and improve mobility", 45);
		trainingRepository.save(training5);

		Nutrition nutrition1 = new Nutrition("High Protein Bulking",
				"Calorie surplus diet focused on lean muscle gain with 2.2g protein per kg bodyweight.",
				"Muscle gain", 3200);
		nutritionRepository.save(nutrition1);

		Nutrition nutrition2 = new Nutrition("Lean Cutting",
				"Calorie deficit diet for fat loss while preserving muscle mass with high protein intake.",
				"Fat loss", 2000);
		nutritionRepository.save(nutrition2);

		Nutrition nutrition3 = new Nutrition("Maintenance",
				"Balanced macronutrient intake to maintain current weight and athletic performance.",
				"Maintain weight", 2500);
		nutritionRepository.save(nutrition3);

		Nutrition nutrition4 = new Nutrition("Strength Focus",
				"High carb, high protein diet optimized for powerlifting and strength performance.",
				"Strength gain", 2800);
		nutritionRepository.save(nutrition4);

		userRepository.save(new User("user", passwordEncoder.encode("pass"), "USER"));
		userRepository.save(new User("admin", passwordEncoder.encode("adminpass"), "USER", "ADMIN"));
	}
}
