package es.codeurjc.daw.powergym.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.powergym.model.Training;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    Optional<Training> findById(long id);
    List<Training> findByName(String name);
    Page<Training> findById(Long id, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Optional<Training> findByWithUserById(Long id);

}

