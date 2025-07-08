package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class TeacherServiceIT {

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private TeacherRepository teacherRepository;

	private Teacher teacher1;
	private Teacher teacher2;

	@BeforeEach
	void setup() {
		teacherRepository.deleteAll();

		teacher1 = Teacher.builder()
				.lastName("Smith")
				.firstName("John")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		teacher2 = Teacher.builder()
				.lastName("Smithe")
				.firstName("Jane")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		teacherRepository.save(teacher1);
		teacherRepository.save(teacher2);
	}

	@Test
	void testFindAll() {

		List<Teacher> teachers = teacherService.findAll();

		assertThat(teachers).hasSize(2);
		assertThat(teachers).extracting(Teacher::getLastName).contains("Smith", "Smithe");
	}

	@Test
	void findById_existingTeacher() {
		Teacher result = teacherService.findById(teacher1.getId());

		assertThat(result).isNotNull();
		assertThat(result.getLastName()).isEqualTo("Smith");
		assertThat(result.getFirstName()).isEqualTo("John");
	}

	@Test
	void findById_notFoundTeacher() {
		Teacher notFound = teacherService.findById(999L);

		assertThat(notFound).isNull();
	}
}