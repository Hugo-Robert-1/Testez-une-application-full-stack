package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
	@Mock
	private SessionRepository sessionRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private SessionService sessionService;

	@BeforeEach
	void setUp() {
	}

	@Test
	void create_shouldSaveSession() {
		Session session = new Session();
		when(sessionRepository.save(session)).thenReturn(session);

		Session result = sessionService.create(session);

		assertThat(result).isEqualTo(session);
		verify(sessionRepository).save(session);
	}

	@Test
	void delete_shouldCallRepository() {
		sessionService.delete(1L);
		verify(sessionRepository).deleteById(1L);
	}

	@Test
	void findAll_shouldReturnSessions() {
		List<Session> sessions = Arrays.asList(new Session(), new Session());
		when(sessionRepository.findAll()).thenReturn(sessions);

		List<Session> result = sessionService.findAll();

		assertThat(result).isEqualTo(sessions);
	}

	@Test
	void getById_shouldReturnSession() {
		Session session = new Session();
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

		Session result = sessionService.getById(1L);

		assertThat(result).isEqualTo(session);
	}

	@Test
	void getById_shouldReturnNullIfNotFound() {
		when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

		Session result = sessionService.getById(1L);

		assertThat(result).isNull();
	}

	@Test
	void update_shouldSaveSessionWithId() {
		Session session = new Session();
		Session savedSession = new Session();
		savedSession.setId(1L);

		when(sessionRepository.save(any(Session.class))).thenReturn(savedSession);

		Session result = sessionService.update(1L, session);

		assertThat(result.getId()).isEqualTo(1L);
		verify(sessionRepository).save(session);
	}

	@Test
	void participate_shouldAddUserToSession() {
		User user = new User();
		user.setId(2L);
		Session session = new Session();
		session.setId(1L);
		session.setUsers(new ArrayList<>());

		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
		when(userRepository.findById(2L)).thenReturn(Optional.of(user));
		when(sessionRepository.save(any(Session.class))).thenReturn(session);

		sessionService.participate(1L, 2L);

		assertThat(session.getUsers()).contains(user);
		verify(sessionRepository).save(session);
	}

	@Test
	void participate_shouldThrowNotFoundExceptionIfSessionOrUserNotFound() {
		when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> sessionService.participate(1L, 2L)).isInstanceOf(NotFoundException.class);

		Session session = new Session();
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
		when(userRepository.findById(2L)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> sessionService.participate(1L, 2L)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void participate_shouldThrowBadRequestIfAlreadyParticipate() {
		User user = new User();
		user.setId(2L);
		Session session = new Session();
		session.setUsers(new ArrayList<>(Collections.singletonList(user)));

		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
		when(userRepository.findById(2L)).thenReturn(Optional.of(user));

		assertThatThrownBy(() -> sessionService.participate(1L, 2L)).isInstanceOf(BadRequestException.class);
	}

	@Test
	void noLongerParticipate_shouldRemoveUserFromSession() {
		User user = new User();
		user.setId(2L);
		Session session = new Session();
		session.setUsers(new ArrayList<>(Collections.singletonList(user)));

		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
		when(sessionRepository.save(any(Session.class))).thenReturn(session);

		sessionService.noLongerParticipate(1L, 2L);

		assertThat(session.getUsers()).doesNotContain(user);
		verify(sessionRepository).save(session);
	}

	@Test
	void noLongerParticipate_shouldThrowNotFoundIfSessionNotFound() {
		when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> sessionService.noLongerParticipate(1L, 2L)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void noLongerParticipate_shouldThrowBadRequestIfUserNotParticipating() {
		Session session = new Session();
		session.setUsers(new ArrayList<>());

		when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

		assertThatThrownBy(() -> sessionService.noLongerParticipate(1L, 2L)).isInstanceOf(BadRequestException.class);
	}

	@Test
	void noLongerParticipate_shouldRemoveUserFromSession2() {
		Long sessionId = 1L;
		Long userIdToRemove = 2L;
		LocalDateTime localDateTime = LocalDateTime.now();

		User user1 = User.builder()
				.id(1L)
				.email("test@test.com")
				.firstName("John")
				.lastName("Doe")
				.password("password")
				.admin(false)
				.createdAt(localDateTime)
				.updatedAt(localDateTime)
				.build();

		User user2 = User.builder()
				.id(userIdToRemove)
				.email("test2@test.com")
				.firstName("John")
				.lastName("Doe")
				.password("password")
				.admin(false)
				.createdAt(localDateTime)
				.updatedAt(localDateTime)
				.build();

		Session session = Session.builder().id(sessionId).users(Arrays.asList(user1, user2)).build();

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
		when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

		sessionService.noLongerParticipate(sessionId, userIdToRemove);

		assertThat(session.getUsers())
				.extracting(User::getId)
				.doesNotContain(userIdToRemove)
				.contains(1L);
	}
}
