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

    public ProgressRestController(TrainingService trainingService, NutritionService nutritionService, UserService userService) {
        this.trainingService = trainingService;
        this.nutritionService = nutritionService;
        this.userService = userService;
    }

    @GetMapping("/chart")
        public ResponseEntity<ProgressChartResponse> getProgressChart(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User currentUser = userService.findByEmail(authentication.getName());

        int trainingsCount = trainingService.findBySubscriber(currentUser).size();
        int nutritionsCount = nutritionService.findBySubscriber(currentUser).size();

        int totalTrainingMinutes = trainingService.findBySubscriber(currentUser)
                .stream()
                .mapToInt(training -> Math.max(training.getTime(), 0))
                .sum();

        int totalNutritionCalories = nutritionService.findBySubscriber(currentUser)
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

                ProgressChartResponse response = new ProgressChartResponse("bar", labels, values, summary);
        return ResponseEntity.ok(response);
    }

        private record ProgressChartResponse(
                        String chartType,
                        List<String> labels,
                        List<Integer> values,
                        Map<String, Integer> summary) {
        }
}
