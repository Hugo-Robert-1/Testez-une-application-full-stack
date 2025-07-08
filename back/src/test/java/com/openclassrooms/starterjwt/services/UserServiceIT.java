package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceIT {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	public void setUp() {
		userRepository.deleteAll();

		user = new User();
		user.setEmail("user@email.com");
	}

	@Test
	public void findById_shouldFindUser() {
		User newUser = userRepository.save(user);

		User foundUser = userService.findById(newUser.getId());

		assertNotNull(foundUser);
		assertEquals(newUser.getEmail(), foundUser.getEmail());
	}

	@Test
	public void findById_shouldNotFound() {
		User foundUser = userService.findById(999L);

		assertNull(foundUser);
	}

	@Test
	public void deleteById_shouldSuccessfullyDelete() {
		User newUser = userRepository.save(user);

		User foundUser = userService.findById(newUser.getId());

		assertNotNull(foundUser);
		assertEquals(newUser.getEmail(), foundUser.getEmail());
		assertEquals(newUser.getId(), foundUser.getId());

		userService.delete(foundUser.getId());

		User deletedUser = userService.findById(newUser.getId());

		assertNull(deletedUser);
	}
}