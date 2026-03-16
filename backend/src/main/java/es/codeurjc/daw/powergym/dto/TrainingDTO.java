package es.codeurjc.daw.powergym.dto;

public record TrainingDTO(
    Long id,
    String name,
    String description,
    String goal,
    Integer time,
    ImageDTO image,
    Long userId) {
    
}
