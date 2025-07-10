package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void cleanDb() {
		userRepository.deleteAll();
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void signup_shouldReturn400_whenEmailIsAlreadyUsed() throws Exception {
		SignupRequest request = new SignupRequest();
		request.setEmail("john.doe@email.com");
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setPassword("azerty51");

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("User registered successfully!"));

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
	}

	@Test
	void signup_shouldReturn200_whenSignupIsValid() throws Exception {
		SignupRequest request = new SignupRequest();
		request.setEmail("john.doe@email.com");
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setPassword("azerty51");

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("User registered successfully!"));
	}

	@Test
	void authenticateUser_shouldReturn200_whenLoginIsValid() throws Exception {
		SignupRequest request = new SignupRequest();
		request.setEmail("john.doe@email.com");
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setPassword("azerty51");

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("User registered successfully!"));

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("john.doe@email.com");
		loginRequest.setPassword("azerty51");

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk());
	}

	@Test
	void registerUser_shouldReturnErrorIfMissingValue() throws Exception {
		SignupRequest signUpRequest = new SignupRequest();
		signUpRequest.setEmail("user@email.com");
		signUpRequest.setFirstName("User");
		signUpRequest.setLastName(null);
		signUpRequest.setPassword("password");

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void authenticateUser_shouldReturnErrorIfMissingValue() throws Exception {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("user@email.com");
		loginRequest.setPassword(null);

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isBadRequest());
	}
}