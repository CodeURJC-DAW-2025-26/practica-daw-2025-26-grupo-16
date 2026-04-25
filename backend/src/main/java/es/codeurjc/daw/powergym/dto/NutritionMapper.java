package es.codeurjc.daw.powergym.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.codeurjc.daw.powergym.model.Nutrition;

@Component
public class NutritionMapper {

    @Autowired
    private ImageMapper imageMapper;

    public NutritionDTO toDTO(Nutrition nutrition) {

        return new NutritionDTO(
                nutrition.getId(),
                nutrition.getName(),
                nutrition.getDescription(),
                nutrition.getGoal(),
                nutrition.getCalories(),
                nutrition.getImage() != null ? imageMapper.toDTO(nutrition.getImage()) : null,
                nutrition.getUser() != null ? nutrition.getUser().getId() : null,
                false
        );
    }

    public Nutrition toDomain(NutritionDTO dto) {

        Nutrition nutrition = new Nutrition();

        nutrition.setId(dto.id());
        nutrition.setName(dto.name());
        nutrition.setDescription(dto.description());
        nutrition.setGoal(dto.goal());
        nutrition.setCalories(dto.calories());

        return nutrition;
    }
}