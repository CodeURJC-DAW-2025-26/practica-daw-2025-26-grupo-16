package es.codeurjc.daw.powergym.dto;

import org.mapstruct.Mapper;

import es.codeurjc.daw.powergym.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
    
}
