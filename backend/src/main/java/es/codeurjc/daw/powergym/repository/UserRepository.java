package es.codeurjc.daw.powergym.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.powergym.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);
    Optional<User> findByName(String name);
    Page<User> findById(Long id, Pageable pageable);
}


