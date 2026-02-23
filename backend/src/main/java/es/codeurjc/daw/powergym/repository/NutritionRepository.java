package es.codeurjc.daw.powergym.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.powergym.model.Nutrition;

public interface NutritionRepository extends JpaRepository<Nutrition, Long> {

    Optional<Nutrition> findById(long id);
    List<Nutrition> findByName(String name);
    Page<Nutrition> findById(Long id, Pageable pageable);

    /*@EntityGraph(attributePaths = "user")
    Optional<Nutrition> findByWithUserById(Long id);*/

}
