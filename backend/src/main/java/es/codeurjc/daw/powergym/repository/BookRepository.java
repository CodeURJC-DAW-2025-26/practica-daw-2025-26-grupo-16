package es.codeurjc.daw.powergym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.powergym.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}