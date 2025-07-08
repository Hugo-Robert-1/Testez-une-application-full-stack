package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
	@Mock
	private UserService userService;

	@Mock
	private UserMapper userMapper;

	@InjectMocks
	private UserController userController;

	private User user;
	private UserDto userDto;
	private final Long userId = 1l;

	@BeforeEach
	private void setup() {
		user = new User();
		user.setId(userId);
		user.setEmail("user@email.com");

		userDto = new UserDto();
		userDto.setId(userId);
		userDto.setEmail("user@email.com");
	}

	@Test
	void findById_shouldReturnDto() {
		when(userService.findById(userId)).thenReturn(user);
		when(userMapper.toDto(user)).thenReturn(userDto);

		ResponseEntity<?> response = userController.findById(userId.toString());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(userDto, response.getBody());
	}

	@Test
	void findById_SessionNotFound() {
		when(userService.findById(userId)).thenReturn(null);

		ResponseEntity<?> response = userController.findById(userId.toString());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void findById_shouldReturnBadRequest() {
		// Call the method with an invalid ID
		ResponseEntity<?> response = userController.findById("id");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void delete_shouldReturnOk() {
		UserDetails userDetails = mock(UserDetails.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		when(userService.findById(userId)).thenReturn(user);
		when(userDetails.getUsername()).thenReturn("user@email.com");
		when(authentication.getPrincipal()).thenReturn(userDetails);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		ResponseEntity<?> response = userController.save(userId.toString());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(userService).delete(userId);
	}

	@Test
	void delete_shouldReturnNotFound() {
		when(userService.findById(userId)).thenReturn(null);

		ResponseEntity<?> response = userController.save(userId.toString());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void delete_shouldReturnBadRequest() {
		ResponseEntity<?> response = userController.save("id");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void delete_shouldReturnUnauthorizedIfCurrentAuthentififedUserDeletingAnotherUser() {
		UserDetails userDetails = mock(UserDetails.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		when(userService.findById(userId)).thenReturn(user);
		when(userDetails.getUsername()).thenReturn("johndoe@email.com");
		when(authentication.getPrincipal()).thenReturn(userDetails);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		ResponseEntity<?> response = userController.save(userId.toString());

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
}
