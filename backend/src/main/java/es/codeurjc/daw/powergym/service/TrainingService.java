package es.codeurjc.daw.powergym.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.powergym.exception.NotFoundException;
import es.codeurjc.daw.powergym.model.Image;
import es.codeurjc.daw.powergym.model.Nutrition;
import es.codeurjc.daw.powergym.model.Training;
import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.repository.TrainingRepository;

@Service
public class TrainingService {

	@Autowired
	private TrainingRepository trainingRepository;

    @Autowired
	private UserService userRepository;

	public Optional<Training> findById(long id) {
		return trainingRepository.findById(id);
	}
	
	public boolean exist(long id) {
		return trainingRepository.existsById(id);
	}

    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    public Page<Training> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        return trainingRepository.findAll(pageable);
    }

    public void save(Training training) {
        trainingRepository.save(training);
    }

    public void delete(long id) {
        trainingRepository.deleteById(id);
    }

    public List<Training> findBySubscriber(User user) {
        return trainingRepository.findBySubscribersContains(user);
    }

    // Owner-based retrieval: trainings owned by a user
    public List<Training> findByOwner(User user) {
        return trainingRepository.findByUser(user);
    }

    public Optional<Training> findByIdWithUser(long id) {
		return trainingRepository.findWithUserById(id);
	}

    public Collection<Training> getTrainings() {

		return trainingRepository.findAll();
	}

	public Training getTraining(long id) {

		return trainingRepository.findById(id).orElseThrow(NotFoundException::new);
	}

	public Training createTraining(Training training) {

		if (training.getId() != null) {
			throw new IllegalArgumentException();
		}

		Long userId = training.getUser().getId();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));

		training.setUser(user);

		return trainingRepository.save(training);
	}

	public Training replaceTraining(long id, Training updatedTraining) throws SQLException {

		Training oldTraining = trainingRepository.findById(id).orElseThrow(NotFoundException::new);

		checkOwnerOrAdmin(oldTraining);

		updatedTraining.setId(id);

		if (oldTraining.getImage() != null) {
			// Transfer the image from the old training to the new one
			updatedTraining.setImage(oldTraining.getImage());
		}

		trainingRepository.save(updatedTraining);

		return updatedTraining;
	}

	public Training deleteTraining(long id) {

		Training training = trainingRepository.findById(id).orElseThrow(NotFoundException::new);

		checkOwnerOrAdmin(training);

		trainingRepository.deleteById(id);

		return training;
	}

	public Training addImageToTraining(long id, Image image) {
		Training training = trainingRepository.findById(id).orElseThrow();
		training.setImage(image);
		trainingRepository.save(training);

		return training;
	}

	public Training removeImageFromTraining(long trainingId) {
		Training training = trainingRepository.findById(trainingId).orElseThrow();
		training.setImage(null);
		trainingRepository.save(training);

		return training;
	}

	private void checkOwnerOrAdmin(Training training) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !training.getUser().getEmail().equals(username)) {
            throw new AccessDeniedException("You are not the owner of this training plan or an administrator.");
        }
    }
}
