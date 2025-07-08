package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

	@Mock
	private TeacherRepository teacherRepository;

	@InjectMocks
	private TeacherService teacherService;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findAll_shouldReturnAllTeachers() {
		Teacher teacher1 = new Teacher();
		Teacher teacher2 = new Teacher();
		List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

		when(teacherRepository.findAll()).thenReturn(teachers);

		List<Teacher> result = teacherService.findAll();

		assertThat(result).isEqualTo(teachers);
	}

	@Test
	void findById_shouldReturnTeacherIfExists() {
		Teacher teacher = new Teacher();
		teacher.setId(1L);

		when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

		Teacher result = teacherService.findById(1L);

		assertThat(result).isEqualTo(teacher);
	}

	@Test
	void findById_shouldReturnNullIfNotExists() {
		when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

		Teacher result = teacherService.findById(1L);

		assertThat(result).isNull();
	}
}
