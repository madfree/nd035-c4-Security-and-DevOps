package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	public static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		ResponseEntity<User> userResponse = ResponseEntity.of(userRepository.findById(id));
		if (userResponse.getStatusCode().isError()) {
			log.warn("User for id not found: " + id);
		} else {
			log.debug("Found user with name: " + userResponse.getBody().getUsername());
		}
		return userResponse;
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			log.warn("User not found: " + username);
		} else {
			log.debug("Found user with name: " + user.getUsername());
		}
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		String password = createUserRequest.getPassword();
		if (!passwordValid(password, createUserRequest.getConfirmPassword())) {
			log.debug("Tried to create username with invalid password");
			return ResponseEntity.badRequest().build();
		} else {
			user.setPassword(bCryptPasswordEncoder.encode(password));
		}

		Cart cart = new Cart();
		cartRepository.save(cart);

		user.setCart(cart);
		userRepository.save(user);

		ResponseEntity<User> userResponse = ResponseEntity.ok(user);
		if (userResponse.getStatusCode().isError()) {
			log.error("Could not create user: " + user.getUsername());
		} else {
			log.info("Created user with name: " + user.getUsername());
		}
		return userResponse;
	}

	private boolean passwordValid(String password, String confirmPassword) {
		return password.length() >= 8 && password.equals(confirmPassword);
	}

}
