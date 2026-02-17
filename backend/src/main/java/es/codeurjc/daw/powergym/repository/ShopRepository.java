package es.codeurjc.daw.powergym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.powergym.model.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {

}