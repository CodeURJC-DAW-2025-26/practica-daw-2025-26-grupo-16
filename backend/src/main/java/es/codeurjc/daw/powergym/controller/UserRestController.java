package es.codeurjc.daw.powergym.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.codeurjc.daw.powergym.dto.UserDTO;
import es.codeurjc.daw.powergym.dto.UserMapper;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(userMapper.toDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {

        User user = userMapper.toEntity(dto);

        userService.save(user);

        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UserDTO dto) {

        return userService.findById(id).map(user -> {

            user.setName(dto.name());
            user.setRoles(dto.roles());

            userService.save(user);

            return ResponseEntity.ok(userMapper.toDTO(user));

        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {

        if (!userService.exist(id)) {
            return ResponseEntity.notFound().build();
        }

        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
