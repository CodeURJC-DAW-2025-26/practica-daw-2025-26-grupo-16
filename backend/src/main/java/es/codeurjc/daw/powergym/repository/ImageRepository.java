package es.codeurjc.daw.powergym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.powergym.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
