package es.codeurjc.daw.powergym.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.daw.powergym.dto.NutritionDTO;
import es.codeurjc.daw.powergym.dto.NutritionMapper;
import es.codeurjc.daw.powergym.dto.TrainingDTO;
import es.codeurjc.daw.powergym.dto.TrainingMapper;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.service.NutritionService;
import es.codeurjc.daw.powergym.service.TrainingService;
import es.codeurjc.daw.powergym.service.UserService;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressRestController {

    private final TrainingService trainingService;
    private final NutritionService nutritionService;
    private final UserService userService;
        private final TrainingMapper trainingMapper;
        private final NutritionMapper nutritionMapper;

        public ProgressRestController(TrainingService trainingService, NutritionService nutritionService, UserService userService,
                        TrainingMapper trainingMapper, NutritionMapper nutritionMapper) {
        this.trainingService = trainingService;
        this.nutritionService = nutritionService;
        this.userService = userService;
                this.trainingMapper = trainingMapper;
                this.nutritionMapper = nutritionMapper;
    }

    @GetMapping("/chart")
        public ResponseEntity<ProgressChartResponse> getProgressChart(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User currentUser = userService.findByEmail(authentication.getName());
                if (currentUser == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                List<es.codeurjc.daw.powergym.model.Training> trainingSubscriptions = trainingService.findBySubscriber(currentUser);
                List<es.codeurjc.daw.powergym.model.Nutrition> nutritionSubscriptions = nutritionService.findBySubscriber(currentUser);

                List<TrainingDTO> subscribedTrainings = trainingSubscriptions
                .stream()
                .map(trainingMapper::toDTO)
                .toList();
                List<NutritionDTO> subscribedNutritions = nutritionSubscriptions
                .stream()
                .map(nutritionMapper::toDTO)
                .toList();

        int trainingsCount = subscribedTrainings.size();
        int nutritionsCount = subscribedNutritions.size();

		int totalTrainingMinutes = trainingSubscriptions
                .stream()
                .mapToInt(training -> Math.max(training.getTime(), 0))
                .sum();

		int totalNutritionCalories = nutritionSubscriptions
                .stream()
                .mapToInt(nutrition -> Math.max(nutrition.getCalories(), 0))
                .sum();

        int averageTrainingMinutes = trainingsCount > 0
                ? Math.round((float) totalTrainingMinutes / trainingsCount)
                : 0;

        int averageCalories = nutritionsCount > 0
                ? Math.round((float) totalNutritionCalories / nutritionsCount)
                : 0;

        int consistency = Math.min((trainingsCount + nutritionsCount) * 12, 100);

                String level;
                if (consistency < 35) {
                        level = "Beginner";
                } else if (consistency < 70) {
                        level = "Intermediate";
                } else {
                        level = "Advanced";
                }

        List<String> labels = List.of(
                "Subscribed Trainings",
                "Subscribed Nutritions",
                "Average Training Minutes",
                "Average Calories",
                "Consistency"
        );

        List<Integer> values = List.of(
                trainingsCount,
                nutritionsCount,
                averageTrainingMinutes,
                averageCalories,
                consistency
        );

        Map<String, Integer> summary = new LinkedHashMap<>();
        summary.put("trainingsCount", trainingsCount);
        summary.put("nutritionsCount", nutritionsCount);
        summary.put("averageTrainingMinutes", averageTrainingMinutes);
        summary.put("averageCalories", averageCalories);
        summary.put("consistency", consistency);

        ProgressChartResponse response = new ProgressChartResponse(
                "bar",
                labels,
                values,
                summary,
                level,
                subscribedTrainings,
                subscribedNutritions);
        return ResponseEntity.ok(response);
    }

    private record ProgressChartResponse(
            String chartType,
            List<String> labels,
            List<Integer> values,
            Map<String, Integer> summary,
            String level,
            List<TrainingDTO> subscribedTrainings,
            List<NutritionDTO> subscribedNutritions) {
    }
}
