package es.codeurjc.daw.powergym.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.codeurjc.daw.powergym.model.Training;

@Component
public class TrainingMapper {

    @Autowired
    private ImageMapper imageMapper;

    public TrainingDTO toDTO(Training training) {

        return new TrainingDTO(
                training.getId(),
                training.getName(),
                training.getDescription(),
                training.getGoal(),
                training.getTime(),
                training.getImage() != null ? imageMapper.toDTO(training.getImage()) : null,
                training.getUser() != null ? training.getUser().getId() : null,
                false
        );
    }

    public Training toDomain(TrainingDTO dto) {

        Training training = new Training();

        training.setId(dto.id());
        training.setName(dto.name());
        training.setDescription(dto.description());
        training.setGoal(dto.goal());
        training.setTime(dto.time());

        return training;
    }
}