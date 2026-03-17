package es.codeurjc.daw.powergym.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.codeurjc.daw.powergym.dto.TrainingDTO;
import es.codeurjc.daw.powergym.dto.TrainingMapper;
import es.codeurjc.daw.powergym.model.Training;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.service.TrainingService;
import es.codeurjc.daw.powergym.service.UserService;

@RestController
@RequestMapping("/api/trainings")
public class TrainingRestController {

    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;
    private final UserService userService;

    public TrainingRestController(TrainingService trainingService,
                                   TrainingMapper trainingMapper,
                                   UserService userService) {
        this.trainingService = trainingService;
        this.trainingMapper = trainingMapper;
        this.userService = userService;
    }

    @GetMapping
    public List<TrainingDTO> getAll() {
        return trainingMapper.toDTOs(trainingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingDTO> getById(@PathVariable long id) {
        return trainingService.findById(id)
                .map(n -> ResponseEntity.ok(trainingMapper.toDTO(n)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TrainingDTO> create(@RequestBody TrainingDTO dto) {

        Training training = trainingMapper.toDomain(dto);

        if (dto.userId() != null) {
            User user = userService.findById(dto.userId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            training.setUser(user);
        }

        trainingService.save(training);

        return ResponseEntity.ok(trainingMapper.toDTO(training));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingDTO> update(@PathVariable long id,
                                              @RequestBody TrainingDTO dto) {

        return trainingService.findById(id).map(existing -> {

            existing.setName(dto.name());
            existing.setDescription(dto.description());
            existing.setGoal(dto.goal());
            existing.setTime(dto.time());

            if (dto.userId() != null) {
                User user = userService.findById(dto.userId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                existing.setUser(user);
            }

            trainingService.save(existing);

            return ResponseEntity.ok(trainingMapper.toDTO(existing));

        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {

        if (!trainingService.exist(id)) {
            return ResponseEntity.notFound().build();
        }

        trainingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}