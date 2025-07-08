package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

public class AuthTokenFilterTest {
	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private UserDetailsServiceImpl userDetailsService;

	@InjectMocks
	private AuthTokenFilter authTokenFilter;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		// Nettoie le contexte de sécurité avant chaque test
		SecurityContextHolder.clearContext();
	}

	@Test
	void doFilterInternal_shouldAuthenticateWhenJwtIsValid() throws Exception {
		String jwt = "valid.jwt.token";
		String username = "testuser";
		UserDetails userDetails = new User(username, "password", Collections.emptyList());

		when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
		when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
		when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);
		when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

		authTokenFilter.doFilterInternal(request, response, filterChain);

		assertThat(SecurityContextHolder.getContext().getAuthentication())
				.isNotNull();
		verify(filterChain).doFilter(request, response);
	}

	@Test
	void doFilterInternal_shouldNotAuthenticateWhenJwtIsInvalid() throws Exception {
		String jwt = "invalid.jwt.token";

		when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
		when(jwtUtils.validateJwtToken(jwt)).thenReturn(false);

		authTokenFilter.doFilterInternal(request, response, filterChain);

		assertThat(SecurityContextHolder.getContext().getAuthentication())
				.isNull();
		verify(filterChain).doFilter(request, response);
	}

	@Test
	void doFilterInternal_shouldNotAuthenticateWhenNoJwt() throws Exception {
		when(request.getHeader("Authorization")).thenReturn(null);

		authTokenFilter.doFilterInternal(request, response, filterChain);

		assertThat(SecurityContextHolder.getContext().getAuthentication())
				.isNull();
		verify(filterChain).doFilter(request, response);
	}

	@Test
	void doFilterInternal_shouldCatchExceptionAndLogError() throws Exception {
		String jwt = "unvalid.jwt.token";

		when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
		when(jwtUtils.validateJwtToken(jwt)).thenThrow(new RuntimeException("Test exception"));

		authTokenFilter.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
	}
}
