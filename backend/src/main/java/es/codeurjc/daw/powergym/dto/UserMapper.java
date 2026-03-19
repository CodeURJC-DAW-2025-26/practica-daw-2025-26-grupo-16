package es.codeurjc.daw.powergym.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.codeurjc.daw.powergym.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    @Mapping(source = "password", target = "encodedPassword")
    User toEntity(UserDTO userDTO);

    default UserDTO toDTOWithoutPassword(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            null,
            user.getRoles(),
            user.getImage() != null ? new ImageDTO(user.getImage().getId()) : null
        );
    }

    default UserDTO toDTOWithPassword(User user, String plainPassword) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            plainPassword,
            user.getRoles(),
            user.getImage() != null ? new ImageDTO(user.getImage().getId()) : null
        );
    }
    
}
