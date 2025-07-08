package com.openclassrooms.starterjwt.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TeacherControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TeacherService teacherService;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private User user;

	@BeforeEach
	void setUp() {
		sessionRepository.deleteAll();
		teacherRepository.deleteAll();
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
	void findById_shouldReturnTeacher_whenExists() throws Exception {
		Teacher mockTeacher = new Teacher();
		mockTeacher.setId(1L);
		mockTeacher.setLastName("Doe");
		mockTeacher.setFirstName("John");

		when(teacherService.findById(1L)).thenReturn(mockTeacher);

		String token = obtainAccessToken("test@test.com", "password");
		mockMvc.perform(get("/api/teacher/1")
				.header("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.firstName").value("John"))
				.andExpect(jsonPath("$.lastName").value("Doe"));
	}

	@Test
	void testFindById_shouldReturn404_whenNotFound() throws Exception {
		when(teacherService.findById(anyLong())).thenReturn(null);

		String token = obtainAccessToken("test@test.com", "password");
		mockMvc.perform(get("/api/teacher/999")
				.header("Authorization", token))
				.andExpect(status().isNotFound());
	}

	@Test
	void testFindById_shouldReturn400_whenInvalidId() throws Exception {
		String token = obtainAccessToken("test@test.com", "password");

		mockMvc.perform(get("/api/teacher/invalid-id")
				.header("Authorization", token))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testFindAll_shouldReturnListOfTeachers() throws Exception {
		String token = obtainAccessToken("test@test.com", "password");

		Teacher teacher1 = new Teacher(1L, "Doe", "John", null, null);
		Teacher teacher2 = new Teacher(2L, "Smith", "Joe", null, null);
		List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

		when(teacherService.findAll()).thenReturn(teachers);

		mockMvc.perform(get("/api/teacher")
				.header("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].firstName").value("John"))
				.andExpect(jsonPath("$[1].lastName").value("Smith"));
	}

	@Test
	void testFindById_shouldReturn401_whenNotAuthenticated() throws Exception {
		mockMvc.perform(get("/api/teacher/1"))
				.andExpect(status().isUnauthorized());
	}
}