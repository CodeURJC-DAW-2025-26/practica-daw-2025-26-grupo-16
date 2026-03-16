package es.codeurjc.daw.powergym.dto;

public record NutritionDTO(
    Long id,
    String name,
    String description,
    String goal,
    Integer calories,
    ImageDTO image,
    Long userId) {
}

