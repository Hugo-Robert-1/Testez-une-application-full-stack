package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private User user;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
		user = User.builder()
				.email("test@test.com")
				.firstName("John")
				.lastName("Doe")
				.password(passwordEncoder.encode("password"))
				.admin(false)
				.build();
		user = userRepository.save(user);
	}

	private String obtainAccessToken(String email, String password) throws Exception {
		Map<String, String> login = new HashMap<>();
		login.put("email", email);
		login.put("password", password);

		String response = mockMvc.perform(
				post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(login)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		return "Bearer " + new ObjectMapper().readTree(response).get("token").asText();
	}

	@Test
	void getUserById_shouldReturnUser() throws Exception {
		String token = obtainAccessToken("test@test.com", "password");
		mockMvc.perform(get("/api/user/" + user.getId())
				.header("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("test@test.com"));
	}

	@Test
	void getUserById_shouldReturnNotFound() throws Exception {
		String token = obtainAccessToken("test@test.com", "password");
		mockMvc.perform(get("/api/user/99999")
				.header("Authorization", token))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteUserById_shouldReturnOk() throws Exception {
		String token = obtainAccessToken("test@test.com", "password");
		mockMvc.perform(delete("/api/user/" + user.getId())
				.header("Authorization", token))
				.andExpect(status().isOk());
	}
}
