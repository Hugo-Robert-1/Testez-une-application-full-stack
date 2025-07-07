package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

	@Mock
	private TeacherMapper teacherMapper;

	@Mock
	private TeacherService teacherService;

	@InjectMocks
	private TeacherController teacherController;

	private Teacher teacher;
	private TeacherDto teacherDto;
	private final Long teacherId = 1L;

	@BeforeEach
	void setup() {
		teacher = new Teacher();
		teacher.setId(teacherId);
		teacher.setFirstName("John");
		teacher.setLastName("Doe");

		teacherDto = new TeacherDto();
		teacherDto.setId(teacherId);
		teacherDto.setFirstName("John");
		teacherDto.setLastName("Doe");
	}

	@Test
	void findById_shouldReturnTeacher() {
		when(teacherService.findById(teacherId)).thenReturn(teacher);
		when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

		ResponseEntity<?> response = teacherController.findById(teacherId.toString());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(teacherDto, response.getBody());
	}

	@Test
	void findById_shouldReturnTeacherNotFound() {
		when(teacherService.findById(teacherId)).thenReturn(null);

		ResponseEntity<?> response = teacherController.findById(teacherId.toString());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void findById_shouldReturnBadRequest() {
		ResponseEntity<?> response = teacherController.findById("id");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

}
