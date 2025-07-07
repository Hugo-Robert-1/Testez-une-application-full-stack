package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;

@SpringBootTest
public class TeacherMapperTest {

	@Autowired
	private TeacherMapper teacherMapper;

	@Test
	void toEntity_shouldMapTeacherDtoToEntity() {
		TeacherDto teacherDto = new TeacherDto();
		teacherDto.setId(1L);
		teacherDto.setFirstName("John");
		teacherDto.setLastName("Smith");
		teacherDto.setCreatedAt(LocalDateTime.now());
		teacherDto.setUpdatedAt(LocalDateTime.now());

		Teacher teacher = teacherMapper.toEntity(teacherDto);

		assertThat(teacher).isNotNull();
		assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
		assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
		assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
		assertThat(teacher.getCreatedAt()).isEqualTo(teacherDto.getCreatedAt());
		assertThat(teacher.getUpdatedAt()).isEqualTo(teacherDto.getUpdatedAt());
	}

	@Test
	void toEntity_shouldReturnNullWhenteacherDtoIsNull() {
		TeacherDto teacherDto = null;

		Teacher teacher = teacherMapper.toEntity(teacherDto);

		assertThat(teacher).isNull();
	}

	@Test
	void toDto_shouldMapTeacherToDto() {
		Teacher teacher = new Teacher();
		teacher.setId(1L);
		teacher.setFirstName("John");
		teacher.setLastName("Smith");
		teacher.setCreatedAt(LocalDateTime.now());
		teacher.setUpdatedAt(LocalDateTime.now());

		TeacherDto teacherDto = teacherMapper.toDto(teacher);

		assertThat(teacherDto).isNotNull();
		assertThat(teacherDto.getId()).isEqualTo(teacher.getId());
		assertThat(teacherDto.getFirstName()).isEqualTo(teacher.getFirstName());
		assertThat(teacherDto.getLastName()).isEqualTo(teacher.getLastName());
		assertThat(teacherDto.getCreatedAt()).isEqualTo(teacher.getCreatedAt());
		assertThat(teacherDto.getUpdatedAt()).isEqualTo(teacher.getUpdatedAt());
	}

	@Test
	void toDto_shouldReturnNullWhenteacherIsNull() {
		Teacher teacher = null;

		TeacherDto teacherDto = teacherMapper.toDto(teacher);

		assertThat(teacherDto).isNull();
	}

	@Test
	void toEntity_shouldMapTeacherDtoListToEntityList() {
		TeacherDto teacherDto = new TeacherDto();
		teacherDto.setId(1L);
		teacherDto.setFirstName("John");
		teacherDto.setLastName("Smith");

		List<Teacher> teacherList = teacherMapper.toEntity(List.of(teacherDto));

		assertThat(teacherList).isNotNull();
		assertThat(teacherList).hasSize(1);
		assertThat(teacherList.get(0).getId()).isEqualTo(teacherDto.getId());
		assertThat(teacherList.get(0).getFirstName()).isEqualTo(teacherDto.getFirstName());
		assertThat(teacherList.get(0).getLastName()).isEqualTo(teacherDto.getLastName());
		assertThat(teacherList.get(0).getCreatedAt()).isEqualTo(teacherDto.getCreatedAt());
		assertThat(teacherList.get(0).getUpdatedAt()).isEqualTo(teacherDto.getUpdatedAt());
	}

	@Test
	void toEntity_shouldReturnEmptyList_WhenMappingEmptyDtoListToEntityList() {
		List<TeacherDto> emptyList = Collections.emptyList();

		List<Teacher> teacherList = teacherMapper.toEntity(emptyList);

		assertThat(teacherList).isNotNull().isEmpty();
	}

	@Test
	void toEntity_shouldReturnNull_WhenMappingNullDtoListToEntityList() {
		List<TeacherDto> dtoList = null;

		List<Teacher> teacherList = teacherMapper.toEntity(dtoList);

		assertThat(teacherList).isNull();
	}

	@Test
	void toDto_shouldMapTeacherEntityListToDtoList() {
		Teacher teacher = new Teacher();
		teacher.setId(1L);
		teacher.setFirstName("John");
		teacher.setLastName("Smith");

		List<Teacher> teacherList = new ArrayList<>();
		teacherList.add(teacher);

		List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

		assertThat(teacherDtoList).isNotNull();
		assertThat(teacherDtoList).hasSize(1);
		assertThat(teacherDtoList.get(0).getId()).isEqualTo(teacherList.get(0).getId());
	}

	@Test
	void toDto_shouldReturnEmptyList_WhenMappingEmptyEntityListToDtoList() {
		List<Teacher> emptyList = Collections.emptyList();

		List<TeacherDto> teacherDtoList = teacherMapper.toDto(emptyList);

		assertThat(teacherDtoList).isNotNull().isEmpty();
	}

	@Test
	void toDto_shouldReturnNull_WhenMappingNullEntityListToDtoList() {
		List<Teacher> entityList = null;

		List<TeacherDto> teacherDtoList = teacherMapper.toDto(entityList);

		assertThat(teacherDtoList).isNull();
	}
}
