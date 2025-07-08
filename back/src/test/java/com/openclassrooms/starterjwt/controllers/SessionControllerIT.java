package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
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
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	private Session session;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private User user;

	@BeforeEach
	void setUp() {
		sessionRepository.deleteAll();
		teacherRepository.deleteAll();
		Teacher teacher = Teacher.builder()
				.firstName("Fran")
				.lastName("Garcia")
				.build();
		teacher = teacherRepository.save(teacher);

		session = Session.builder()
				.name("Yoga Class")
				.date(new Date())
				.description("A relaxing yoga session")
				.teacher(teacher)
				.build();
		session = sessionRepository.save(session);

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
	void getSessionById_shouldReturnSession() throws Exception {
		String token = obtainAccessToken("test@test.com", "password");
		mockMvc.perform(get("/api/session/" + session.getId())
				.header("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Yoga Class"));
	}

	@Test
	void getAllSessions_shouldReturnList() throws Exception {
		String token = obtainAccessToken("test@test.com", "password");
		mockMvc.perform(get("/api/session")
				.header("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Yoga Class"));
	}
}
