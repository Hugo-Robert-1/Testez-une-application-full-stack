package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

	@InjectMocks
	private SessionMapperImpl sessionMapper;

	@Mock
	private TeacherService teacherService;

	@Mock
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void toDto_shouldMapFieldsCorrectly() {
		User user = new User();
		user.setId(1L);

		Teacher teacher = new Teacher();
		teacher.setId(1L);

		Session session = new Session();
		session.setDescription("desc");
		session.setId(1L);
		session.setName("Yoga");
		session.setDate(new Date());
		session.setTeacher(teacher);
		session.setUsers(List.of(user));

		SessionDto sessionDto = sessionMapper.toDto(session);

		assertThat(sessionDto).isNotNull();
		assertThat(sessionDto.getId()).isEqualTo(session.getId());
		assertThat(sessionDto.getName()).isEqualTo(session.getName());
		assertThat(sessionDto.getDescription()).isEqualTo(session.getDescription());
		assertThat(sessionDto.getCreatedAt()).isEqualTo(session.getCreatedAt());
		assertThat(sessionDto.getTeacher_id()).isEqualTo(session.getTeacher().getId());
		assertThat(sessionDto.getUsers()).containsExactly(user.getId());
	}

	@Test
	void toDto_shouldReturnNullWhenSessionIsNull() {
		Session session = null;

		SessionDto sessionDto = sessionMapper.toDto(session);

		assertThat(sessionDto).isNull();
	}

	@Test
	void toEntity_shouldMapSessionDtoToSession() {
		Teacher teacher = new Teacher();
		teacher.setId(1L);

		User user = new User();
		user.setId(1L);

		SessionDto sessionDto = new SessionDto();
		sessionDto.setId(1L);
		sessionDto.setDescription("Yoga");
		sessionDto.setTeacher_id(teacher.getId());
		sessionDto.setUsers(List.of(user.getId()));

		when(teacherService.findById(anyLong())).thenReturn(teacher);
		when(userService.findById(anyLong())).thenReturn(user);

		Session session = sessionMapper.toEntity(sessionDto);

		assertThat(session).isNotNull();
		assertThat(session.getId()).isEqualTo(sessionDto.getId());
		assertThat(session.getDescription()).isEqualTo(sessionDto.getDescription());
		assertThat(session.getCreatedAt()).isEqualTo(sessionDto.getCreatedAt());
		assertThat(session.getTeacher()).isEqualTo(teacher);
		assertThat(session.getUsers()).containsExactly(user);
	}

	@Test
	void toEntity_shouldReturnNullWhenSessionDtoIsNull() {
		SessionDto sessionDto = null;

		Session session = sessionMapper.toEntity(sessionDto);

		assertThat(session).isNull();
	}

	@Test
	void toDto_shouldMapSessionListToSessionDtoList() {
		Teacher teacher = new Teacher();
		teacher.setId(1L);

		User user = new User();
		user.setId(1L);

		Session session = new Session();
		session.setId(1L);
		session.setDescription("Desc");
		session.setName("Yoga");
		session.setDate(new Date());
		session.setTeacher(teacher);
		session.setUsers(List.of(user));

		List<SessionDto> mappedSessionDtos = sessionMapper.toDto(List.of(session));

		assertThat(mappedSessionDtos).isNotNull();
		assertThat(mappedSessionDtos).hasSize(1);
		assertThat(mappedSessionDtos.get(0).getId()).isEqualTo(session.getId());
		assertThat(mappedSessionDtos.get(0).getDescription()).isEqualTo(session.getDescription());
		assertThat(mappedSessionDtos.get(0).getTeacher_id()).isEqualTo(session.getTeacher().getId());
		assertThat(mappedSessionDtos.get(0).getUsers()).containsExactly(user.getId());
	}

	@Test
	void toDto_shouldReturnNullWhenEntityListIsNull() {
		List<Session> entityList = null;

		List<SessionDto> result = sessionMapper.toDto(entityList);

		assertThat(result).isNull();
	}

	@Test
	void toEntity_shouldMapSessionDtoListToSessionList() {
		Teacher teacher = new Teacher();
		teacher.setId(3L);

		User user = new User();
		user.setId(1L);

		SessionDto sessionDto = new SessionDto();
		sessionDto.setId(1L);
		sessionDto.setDescription("Yoga");
		sessionDto.setTeacher_id(teacher.getId());
		sessionDto.setUsers(List.of(user.getId()));

		when(teacherService.findById(anyLong())).thenReturn(teacher);
		when(userService.findById(anyLong())).thenReturn(user);

		List<Session> SessionList = sessionMapper.toEntity(List.of(sessionDto));

		assertThat(SessionList).isNotNull();
		assertThat(SessionList).hasSize(1);
		assertThat(SessionList.get(0).getId()).isEqualTo(sessionDto.getId());
		assertThat(SessionList.get(0).getDescription()).isEqualTo(sessionDto.getDescription());
		assertThat(SessionList.get(0).getTeacher()).isEqualTo(teacher);
		assertThat(SessionList.get(0).getUsers()).containsExactly(user);
	}

	@Test
	void toEntity_shouldReturnNullWhenDtoListIsNull() {
		List<SessionDto> dtoList = null;

		List<Session> result = sessionMapper.toEntity(dtoList);

		assertThat(result).isNull();
	}

	private Long invokeSessionTeacherId(Session session) throws Exception {
		Method method = SessionMapperImpl.class.getDeclaredMethod("sessionTeacherId", Session.class);
		method.setAccessible(true);
		return (Long) method.invoke(sessionMapper, session);
	}

	@Test
	void sessionTeacherId_shouldReturnNull_whenSessionIsNull() throws Exception {
		assertThat(invokeSessionTeacherId(null)).isNull();
	}

	@Test
	void sessionTeacherId_shouldReturnNull_whenTeacherIsNull() throws Exception {
		Session session = new Session();
		session.setTeacher(null);
		assertThat(invokeSessionTeacherId(session)).isNull();
	}

	@Test
	void sessionTeacherId_shouldReturnNull_whenTeacherIdIsNull() throws Exception {
		Teacher teacher = new Teacher();
		teacher.setId(null);
		Session session = new Session();
		session.setTeacher(teacher);
		assertThat(invokeSessionTeacherId(session)).isNull();
	}

	@Test
	void sessionTeacherId_shouldReturnTeacherId_whenPresent() throws Exception {
		Teacher teacher = new Teacher();
		teacher.setId(42L);
		Session session = new Session();
		session.setTeacher(teacher);
		assertThat(invokeSessionTeacherId(session)).isEqualTo(42L);
	}
}