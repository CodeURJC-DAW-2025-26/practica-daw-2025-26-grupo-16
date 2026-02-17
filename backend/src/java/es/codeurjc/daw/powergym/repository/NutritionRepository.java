package es.codeurjc.daw.alphagym.repository;

import java.util.Optional;

import org.hibernate.query.Page;

public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    Optional<Nutrition> findById(long id);
    List<Nutrition> findByNutritionName(String name); 
    Page<Nutrition> findById(long id, Pageable pageable);   
}
