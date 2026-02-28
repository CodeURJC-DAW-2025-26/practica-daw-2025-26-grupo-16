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

    // find all trainings where the given user is in the subscribers set
    List<Training> findBySubscribersContains(es.codeurjc.daw.powergym.model.User user);

    // Find trainings owned by a specific user
    List<Training> findByUser(es.codeurjc.daw.powergym.model.User user);

    @EntityGraph(attributePaths = "user")
    Optional<Training> findWithUserById(Long id);

}

