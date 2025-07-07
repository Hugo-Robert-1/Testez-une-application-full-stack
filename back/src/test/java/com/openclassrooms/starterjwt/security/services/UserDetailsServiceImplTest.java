package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	private User user;

	@BeforeEach
	void setUp() {
		LocalDateTime localDateTime = LocalDateTime.now();

		user = User.builder()
				.id(1L)
				.email("test@test.com")
				.firstName("John")
				.lastName("Doe")
				.password("password")
				.admin(false)
				.createdAt(localDateTime)
				.updatedAt(localDateTime)
				.build();
	}

	@Test
	void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
		when(userRepository.findByEmail("test@test.com"))
				.thenReturn(Optional.of(user));

		UserDetails userDetails = userDetailsService.loadUserByUsername("test@test.com");

		assertThat(userDetails).isNotNull();
		assertThat(userDetails.getUsername()).isEqualTo("test@test.com");
		assertThat(userDetails.getPassword()).isEqualTo("password");

		verify(userRepository, times(1)).findByEmail("test@test.com");
	}

	@Test
	void loadUserByUsername_shouldThrowException_whenUserNotFound() {
		when(userRepository.findByEmail("notfound@test.com"))
				.thenReturn(Optional.empty());

		assertThatThrownBy(() -> userDetailsService.loadUserByUsername("notfound@test.com"))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessageContaining("User Not Found with email: notfound@test.com");

		verify(userRepository, times(1)).findByEmail("notfound@test.com");
	}
}
