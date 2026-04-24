package es.codeurjc.daw.powergym.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.codeurjc.daw.powergym.model.Nutrition;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = ImageMapper.class)
public interface NutritionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "image", target = "image")
    NutritionDTO toDTO(Nutrition nutrition);

    List<NutritionDTO> toDTOs(Collection<Nutrition> nutritions);

    @Mapping(source = "userId", target = "user.id")

    @Mapping(target = "subscribers", ignore = true)
    @Mapping(target = "image", ignore = true)
    Nutrition toDomain(NutritionDTO nutritionDTO);
}
