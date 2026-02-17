package es.codeurjc.daw.alphagym.repository;

import java.util.Optional;

import org.hibernate.query.Page;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    Optional<Training> findById(long id);
    List<Training> findByTrainingName(String name); 
    Page<Training> findById(long id, Pageable pageable);   
}
