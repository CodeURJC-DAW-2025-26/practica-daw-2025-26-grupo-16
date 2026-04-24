package es.codeurjc.daw.powergym.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import es.codeurjc.daw.powergym.dto.ImageDTO;
import es.codeurjc.daw.powergym.dto.UserDTO;
import es.codeurjc.daw.powergym.dto.UserMapper;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserRestController(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public List<UserDTO> getUsers() {
        return userService.findAll()
                .stream()
                .map(userMapper::toDTOWithoutPassword)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(userMapper.toDTOWithoutPassword(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = userService.findByEmail(authentication.getName());
        return ResponseEntity.ok(userMapper.toDTOWithoutPassword(currentUser));
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {

        
        User user = userMapper.toEntity(dto);
        user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));

        User saved = userService.save(user);

        UserDTO responseDto = new UserDTO(
            saved.getId(),
            saved.getName(),
            saved.getEmail(),
            dto.password(), 
            saved.getRoles(),
            saved.getImage() != null ? new ImageDTO(saved.getImage().getId()) : null
        );

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody UserDTO dto) {

        return userService.findById(id).map(user -> {

            user.setName(dto.name());
            user.setEmail(dto.email());

            userService.save(user);

            return ResponseEntity.ok(userMapper.toDTO(user));

        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable long id) {

        return userService.findById(id).map(user -> {

            UserDTO userDTO = userMapper.toDTOWithoutPassword(user);

            userService.delete(id);

            return ResponseEntity.ok(userDTO); 

        }).orElse(ResponseEntity.notFound().build());
    }
}
