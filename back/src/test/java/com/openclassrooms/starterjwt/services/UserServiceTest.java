package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private User user;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		user.setEmail("user@email.com");
	}

	@Test
	void delete_shouldCallRepository() {
		userService.delete(1L);

		verify(userRepository).deleteById(1L);
	}

	@Test
	void findById_shouldReturnUserIfExists() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		User result = userService.findById(1L);

		assertThat(result).isEqualTo(user);
		verify(userRepository).findById(1L);
	}

	@Test
	void findById_shouldReturnNullIfNotExists() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		User result = userService.findById(1L);

		assertThat(result).isNull();
		verify(userRepository).findById(1L);
	}
}