package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@Import(SessionService.class)
@Transactional
@ActiveProfiles("test")
class SessionServiceIT {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private UserRepository userRepository;

	private User user;
	private Session session;

	@BeforeEach
	void setUp() {
		sessionRepository.deleteAll();
		userRepository.deleteAll();

		user = new User();
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.doe@email.com");
		user.setPassword("password");
		user.setAdmin(false);
		user = userRepository.save(user);

		session = new Session();
		session.setName("Yoga");
		session.setDescription("Yoga session");
		session.setDate(new Date());
		session.setUsers(new ArrayList<>());
		session = sessionRepository.save(session);
	}

	@Test
	void create_shouldPersistSession() {
		Session newSession = new Session();
		newSession.setName("Pilates");
		newSession.setDescription("Pilates session");
		newSession.setDate(new Date());
		newSession.setUsers(new ArrayList<>());

		Session saved = sessionService.create(newSession);

		assertThat(saved.getId()).isNotNull();
		assertThat(sessionRepository.findById(saved.getId())).isPresent();
	}

	@Test
	void delete_shouldRemoveSession() {
		sessionService.delete(session.getId());
		assertThat(sessionRepository.findById(session.getId())).isNotPresent();
	}

	@Test
	void findAll_shouldReturnSessions() {
		List<Session> sessions = sessionService.findAll();
		assertThat(sessions).extracting(Session::getName).contains("Yoga");
	}

	@Test
	void getById_shouldReturnSession() {
		Session found = sessionService.getById(session.getId());
		assertThat(found).isNotNull();
		assertThat(found.getName()).isEqualTo("Yoga");
	}

	@Test
	void getById_shouldReturnNullIfNotFound() {
		Session notFound = sessionService.getById(999L);
		assertThat(notFound).isNull();
	}

	@Test
	void update_shouldUpdateSession() {
		session.setDescription("Updated");
		Session updated = sessionService.update(session.getId(), session);
		assertThat(updated.getDescription()).isEqualTo("Updated");
	}

	@Test
	void participate_shouldAddUserToSession() {
		sessionService.participate(session.getId(), user.getId());
		Session updated = sessionRepository.findById(session.getId()).get();
		assertThat(updated.getUsers()).extracting(User::getId).contains(user.getId());
	}

	@Test
	void participate_shouldThrowNotFoundException() {
		assertThatThrownBy(() -> sessionService.participate(999L, user.getId()))
				.isInstanceOf(NotFoundException.class);
		assertThatThrownBy(() -> sessionService.participate(session.getId(), 999L))
				.isInstanceOf(NotFoundException.class);
	}

	@Test
	void participate_shouldThrowBadRequestExceptionIfAlreadyParticipating() {
		sessionService.participate(session.getId(), user.getId());
		assertThatThrownBy(() -> sessionService.participate(session.getId(), user.getId()))
				.isInstanceOf(BadRequestException.class);
	}

	@Test
	void noLongerParticipate_shouldRemoveUserFromSession() {
		sessionService.participate(session.getId(), user.getId());
		sessionService.noLongerParticipate(session.getId(), user.getId());
		Session updated = sessionRepository.findById(session.getId()).get();
		assertThat(updated.getUsers()).extracting(User::getId).doesNotContain(user.getId());
	}

	@Test
	void noLongerParticipate_shouldThrowNotFoundException() {
		assertThatThrownBy(() -> sessionService.noLongerParticipate(999L, user.getId()))
				.isInstanceOf(NotFoundException.class);
	}

	@Test
	void noLongerParticipate_shouldThrowBadRequestExceptionIfNotParticipating() {
		assertThatThrownBy(() -> sessionService.noLongerParticipate(session.getId(), user.getId()))
				.isInstanceOf(BadRequestException.class);
	}
}