package com.openclassrooms.starterjwt.controllers;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private AuthController authController;

	@BeforeEach
	public void setUp() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void authenticateUser_shouldAuthenticateUser() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("user@email.com");
		loginRequest.setPassword("password");

		UserDetailsImpl userDetails = new UserDetailsImpl(1L, "user@email.com", "User", "Name", false, "password");

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);

		// Mock JWT token generation
		when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");

		User user = new User();
		user.setEmail("user@email.com");
		user.setAdmin(false);

		when(userRepository.findByEmail("user@email.com")).thenReturn(of(user));

		ResponseEntity<?> response = authController.authenticateUser(loginRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JwtResponse jwtResponse = (JwtResponse) response.getBody();
		assertEquals("user@email.com", jwtResponse.getUsername());
		assertEquals("jwt-token", jwtResponse.getToken());
		assertEquals(false, jwtResponse.getAdmin());

	}

	@Test
	void registerUser_shouldCreateNewUser() {
		SignupRequest signUpRequest = new SignupRequest();
		signUpRequest.setEmail("useruser@email.com");
		signUpRequest.setFirstName("User");
		signUpRequest.setLastName("User");
		signUpRequest.setPassword("password");

		when(userRepository.existsByEmail("useruser@email.com")).thenReturn(false);
		when(passwordEncoder.encode("password")).thenReturn("encoded-password");

		ResponseEntity<?> response = authController.registerUser(signUpRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
	}

	@Test
	void registerUser_shouldReturnAlreadyExistingUser() {
		SignupRequest signUpRequest = new SignupRequest();
		signUpRequest.setEmail("useruser@email.com");
		signUpRequest.setFirstName("User");
		signUpRequest.setLastName("User");
		signUpRequest.setPassword("password");

		when(userRepository.existsByEmail("useruser@email.com")).thenReturn(true);

		ResponseEntity<?> response = authController.registerUser(signUpRequest);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Error: Email is already taken!", ((MessageResponse) response.getBody()).getMessage());
	}

}
