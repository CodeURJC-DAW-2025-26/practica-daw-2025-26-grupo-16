package es.codeurjc.daw.powergym.dto;

import java.util.List;

public record UserDTO(
    Long id,
    String name,
    List<String> roles) {

}
