package es.codeurjc.daw.powergym.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.model.Training;
import es.codeurjc.daw.powergym.model.User;
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

    public Page<Training> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(pageable);
    }

    public void save(Training training) {
        repository.save(training);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public List<Training> findBySubscriber(User user) {
        return repository.findBySubscribersContains(user);
    }

    // Owner-based retrieval: trainings owned by a user
    public List<Training> findByOwner(User user) {
        return repository.findByUser(user);
    }

    public Optional<Training> findByIdWithUser(long id) {
		return repository.findWithUserById(id);
	}
}
