package es.codeurjc.daw.powergym.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.powergym.model.Training;
import es.codeurjc.daw.powergym.repository.TrainingRepository;

@Service
public class TrainingService {

	@Autowired
	private TrainingRepository repository;

	public Optional<Training> findById(long id) {
		return repository.findById(id);
	}
	
	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Training> findAll() {
		return repository.findAll();
	}

	public void save(Training training) {
		repository.save(training);
	}

	public void delete(long id) {
		repository.deleteById(id);
	}
}
