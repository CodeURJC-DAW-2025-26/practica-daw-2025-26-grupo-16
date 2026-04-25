package es.codeurjc.daw.powergym.dto;

import jakarta.validation.constraints.NotEmpty;

public record TrainingDTO(
    Long id,
    @NotEmpty
    String name,
    String description,
    String goal,
    Integer time,
    ImageDTO image,
    Long userId,
    Boolean subscribed) {
    
}
