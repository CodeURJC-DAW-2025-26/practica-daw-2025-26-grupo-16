package es.codeurjc.daw.powergym.dto;

import org.mapstruct.Mapper;

import es.codeurjc.daw.powergym.model.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {

	ImageDTO toDTO(Image image);
}
