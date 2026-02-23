package es.codeurjc.daw.powergym.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.repository.NutritionRepository;

@Service
public class NutritionService {

	@Autowired
	private NutritionRepository repository;

	public Optional<Nutrition> findById(long id) {
		return repository.findById(id);
	}
	
	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Nutrition> findAll() {
		return repository.findAll();
	}

	public void save(Nutrition nutrition) {
		repository.save(nutrition);
	}

	public void delete(long id) {
		repository.deleteById(id);
	}
}
