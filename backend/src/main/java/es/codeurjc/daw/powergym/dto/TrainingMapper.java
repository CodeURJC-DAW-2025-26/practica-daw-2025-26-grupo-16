package es.codeurjc.daw.powergym.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.codeurjc.daw.powergym.model.Training;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    @Mapping(source = "user.id", target = "userId")
    TrainingDTO toDTO(Training training);

    List<TrainingDTO> toDTOs(Collection<Training> trainings);

    @Mapping(source = "userId", target = "user.id")

    @Mapping(target = "subscribers", ignore = true)
    @Mapping(target = "image", ignore = true)
    Training toDomain(TrainingDTO trainingDTO);
}

