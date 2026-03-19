package es.codeurjc.daw.powergym.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.powergym.model.User;
import es.codeurjc.daw.powergym.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Optional<User> findById(long id) {
		return userRepository.findById(id);
	}
	
	public boolean exist(long id) {
		return userRepository.existsById(id);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User save(User user) {

		return userRepository.save(user);
	}

	public void delete(long id) {
		userRepository.deleteById(id);
	}

	public User findByName(String name) {
		return userRepository.findByName(name)
			.orElseThrow(() -> new RuntimeException("User not found"));
	}


}
