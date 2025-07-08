package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

class UserMapperTest {

	private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

	@Test
	void toDto_shouldMapFields() {
		User user = User.builder()
				.id(1L)
				.email("test@test.com")
				.firstName("John")
				.lastName("Smith")
				.admin(true)
				.password("secret")
				.build();

		UserDto dto = userMapper.toDto(user);

		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getEmail()).isEqualTo("test@test.com");
		assertThat(dto.getFirstName()).isEqualTo("John");
		assertThat(dto.getLastName()).isEqualTo("Smith");
		assertThat(dto.isAdmin()).isTrue();
	}

	@Test
	void toDto_shouldReturnNullWhenUserIsNull() {
		User user = null;

		UserDto mappedUserDto = userMapper.toDto(user);

		assertThat(mappedUserDto).isNull();
	}

	@Test
	void toEntity_shouldMapFields() {
		UserDto dto = new UserDto();
		dto.setId(2L);
		dto.setEmail("user2@test.com");
		dto.setFirstName("John");
		dto.setLastName("Smith");
		dto.setAdmin(false);
		dto.setPassword("secret");

		User user = userMapper.toEntity(dto);

		assertThat(user.getId()).isEqualTo(2L);
		assertThat(user.getEmail()).isEqualTo("user2@test.com");
		assertThat(user.getFirstName()).isEqualTo("John");
		assertThat(user.getLastName()).isEqualTo("Smith");
		assertThat(user.isAdmin()).isFalse();
	}

	@Test
	void toEntity_shouldReturnNullWhenUserDtoIsNull() {
		UserDto userDto = null;

		User mappedUser = userMapper.toEntity(userDto);

		assertThat(mappedUser).isNull();
	}

	@Test
	void toEntity_shouldConvertUserDtoListToUserEntityList() {
		UserDto dto = new UserDto();
		dto.setId(2L);
		dto.setEmail("user2@test.com");
		dto.setFirstName("Bob");
		dto.setLastName("Smith");
		dto.setAdmin(false);
		dto.setPassword("password123");

		List<UserDto> dtoList = List.of(dto);

		List<User> userList = userMapper.toEntity(dtoList);

		assertThat(userList).isNotNull().hasSize(1);
		assertThat(userList.get(0).getId()).isEqualTo(dto.getId());
		assertThat(userList.get(0).getEmail()).isEqualTo(dto.getEmail());
	}

	@Test
	void toDto_shouldReturnNullWhenUserListIsNull() {
		List<User> userList = null;

		List<UserDto> dtoList = userMapper.toDto(userList);

		assertThat(dtoList).isNull();
	}

	@Test
	void toEntity_shouldConvertUserEntityListToUserDtoList() {
		User user = User.builder()
				.id(1L)
				.email("test@test.com")
				.firstName("John")
				.lastName("Smith")
				.admin(true)
				.password("secret")
				.build();

		List<User> userList = List.of(user);

		List<UserDto> dtoList = userMapper.toDto(userList);

		assertThat(dtoList).isNotNull().hasSize(1);
		assertThat(dtoList.get(0).getId()).isEqualTo(user.getId());
		assertThat(dtoList.get(0).getEmail()).isEqualTo(user.getEmail());
	}

	@Test
	void shouldReturnNullWhenUserListIsNull() {
		List<User> userList = null;

		List<UserDto> dtoList = userMapper.toDto(userList);

		assertThat(dtoList).isNull();
	}

}