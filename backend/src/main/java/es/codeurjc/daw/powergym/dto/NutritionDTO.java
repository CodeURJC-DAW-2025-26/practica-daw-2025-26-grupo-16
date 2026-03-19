package es.codeurjc.daw.powergym.dto;

import jakarta.validation.constraints.NotEmpty;

public record NutritionDTO(
    Long id,
    @NotEmpty
    String name,
    String description,
    String goal,
    Integer calories,
    ImageDTO image,
    Long userId) {
}

