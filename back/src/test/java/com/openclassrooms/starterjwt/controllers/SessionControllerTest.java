package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

	@Mock
	private SessionMapper sessionMapper;

	@Mock
	private SessionService sessionService;

	@InjectMocks
	private SessionController sessionController;

	private Session session;
	private SessionDto sessionDto;
	private final Long sessionId = 1L;

	@BeforeEach
	private void setup() {
		session = new Session();
		session.setId(1L);
		session.setName("Training");
		session.setDate(new Date());
		session.setDescription("desc");

		sessionDto = new SessionDto();
		sessionDto.setId(1L);
		sessionDto.setName("Training");
		sessionDto.setDate(new Date());
		sessionDto.setDescription("desc");
	}

	@Test
	void findById_shouldReturnDto() {
		when(sessionService.getById(sessionId)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);

		ResponseEntity<?> response = sessionController.findById(sessionId.toString());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(sessionDto, response.getBody());
	}

	@Test
	void findById_SessionNotFound() {
		when(sessionService.getById(sessionId)).thenReturn(null);

		ResponseEntity<?> response = sessionController.findById(sessionId.toString());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void findById_shouldReturnBadRequest() {
		// Call the method with an invalid ID
		ResponseEntity<?> response = sessionController.findById("id");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void findAll_shouldReturnAll() {
		List<Session> sessions = Arrays.asList(session);
		when(sessionService.findAll()).thenReturn(sessions);
		when(sessionMapper.toDto(sessions)).thenReturn(Arrays.asList(sessionDto));

		ResponseEntity<?> response = sessionController.findAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Arrays.asList(sessionDto), response.getBody());
	}

	@Test
	void create_shouldReturnSessionCreated() {
		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
		when(sessionService.create(session)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);

		ResponseEntity<?> response = sessionController.create(sessionDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(sessionDto, response.getBody());
	}

	@Test
	void update_ShouldReturnSessionUpdated() {
		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
		when(sessionService.update(sessionId, session)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);

		ResponseEntity<?> response = sessionController.update(sessionId.toString(), sessionDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(sessionDto, response.getBody());
	}

	@Test
	void update_shouldReturnBadRequest() {
		ResponseEntity<?> response = sessionController.update("id", sessionDto);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void delete_shouldReturnOk() {
		when(sessionService.getById(sessionId)).thenReturn(session);

		ResponseEntity<?> response = sessionController.save(sessionId.toString());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(sessionService).delete(sessionId);
	}

	@Test
	void delete_shouldReturnNotFound() {
		when(sessionService.getById(sessionId)).thenReturn(null);

		ResponseEntity<?> response = sessionController.save(sessionId.toString());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void delete_shouldReturnBadRequest() {
		ResponseEntity<?> response = sessionController.save("id");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void participate_shouldReturnOk() {
		ResponseEntity<?> response = sessionController.participate(sessionId.toString(), "1");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(sessionService).participate(sessionId, 1L);
	}

	@Test
	void participate_shouldReturnBadRequest() {
		ResponseEntity<?> response = sessionController.participate("id", "1");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void noLongerParticipate_shouldReturnOk() {
		ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId.toString(), "1");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(sessionService).noLongerParticipate(sessionId, 1L);
	}

	@Test
	void noLongerParticipate_shouldReturnBadRequest() {
		ResponseEntity<?> response = sessionController.noLongerParticipate("id", "1");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

}
