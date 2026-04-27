package es.codeurjc.daw.powergym.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import es.codeurjc.daw.powergym.dto.ImageDTO;
import es.codeurjc.daw.powergym.dto.NutritionDTO;
import es.codeurjc.daw.powergym.dto.NutritionMapper;
import es.codeurjc.daw.powergym.dto.TrainingDTO;
import es.codeurjc.daw.powergym.dto.TrainingMapper;
import es.codeurjc.daw.powergym.dto.UserDTO;
import es.codeurjc.daw.powergym.dto.UserMapper;
import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.model.Training;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.service.ImageService;
import es.codeurjc.daw.powergym.service.NutritionService;
import es.codeurjc.daw.powergym.service.TrainingService;
import es.codeurjc.daw.powergym.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TrainingService trainingService;
    private final NutritionService nutritionService;
    private final TrainingMapper trainingMapper;
    private final NutritionMapper nutritionMapper;
    private final ImageService imageService;
    private final UserDetailsService userDetailsService;
    private final es.codeurjc.daw.powergym.security.jwt.JwtTokenProvider jwtTokenProvider;

    public UserRestController(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder,
            TrainingService trainingService, NutritionService nutritionService,
            TrainingMapper trainingMapper, NutritionMapper nutritionMapper, ImageService imageService,
            UserDetailsService userDetailsService, es.codeurjc.daw.powergym.security.jwt.JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.trainingService = trainingService;
        this.nutritionService = nutritionService;
        this.trainingMapper = trainingMapper;
        this.nutritionMapper = nutritionMapper;
        this.imageService = imageService;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
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

        User currentUser;
        try {
            currentUser = userService.findByEmail(authentication.getName());
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(userMapper.toDTOWithoutPassword(currentUser));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(
            Authentication authentication,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            HttpServletResponse response) {

        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        User currentUser = userService.findByEmail(authentication.getName());
        String oldEmail = currentUser.getEmail();
        Image oldImage = currentUser.getImage();

        if (name != null && !name.isEmpty()) {
            currentUser.setName(name);
        }

        boolean emailChanged = false;
        if (email != null && !email.isEmpty() && !email.equals(oldEmail)) {
            currentUser.setEmail(email);
            emailChanged = true;
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                if (imageFile != null && !imageFile.isEmpty()) {
                    try {
                        Image newImage = imageService.createImage(imageFile.getInputStream());
                        currentUser.setImage(newImage);
                    } catch (IOException e) {
                        return ResponseEntity.status(500).build();
                    }
                }
            } catch (Exception e) {
                return ResponseEntity.status(500).build();
            }
        }

        userService.save(currentUser);

        if (emailChanged) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(currentUser.getEmail());
                String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
                
                Cookie cookie = new Cookie("ACCESS_TOKEN", newAccessToken);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(3600);
                response.addCookie(cookie);
            } catch (Exception e) {
                org.slf4j.LoggerFactory.getLogger(getClass())
                        .warn("Could not refresh JWT token after email change", e);
            }
        }
        User updatedUser = userService.findById(currentUser.getId()).orElse(currentUser);
        return ResponseEntity.ok(userMapper.toDTOWithoutPassword(updatedUser));
    }

    @GetMapping("/me/trainings")
    public ResponseEntity<List<TrainingDTO>> getUserSubscribedTrainings(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.findByEmail(authentication.getName());
        List<Training> trainings = trainingService.findBySubscriber(user);
        
        return ResponseEntity.ok(trainings.stream().map(trainingMapper::toDTO).toList());
    }

    @GetMapping("/me/nutritions")
    public ResponseEntity<List<NutritionDTO>> getUserSubscribedNutritions(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userService.findByEmail(authentication.getName());
        List<Nutrition> nutritions = nutritionService.findBySubscriber(user);
        
        return ResponseEntity.ok(nutritions.stream().map(nutritionMapper::toDTO).toList());
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
