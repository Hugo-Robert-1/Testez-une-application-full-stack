package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AuthEntryPointJwtTest {
	@InjectMocks
	private AuthEntryPointJwt authEntryPointJwt;

	@Test
	void commenceTest() throws IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		AuthenticationException authException = new AuthenticationException("Unauthorized error message") {
		};

		request.setServletPath("/api/unauthorized");
		authEntryPointJwt.commence(request, response, authException);

		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
		assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> responseBody = objectMapper.readValue(response.getContentAsString(), Map.class);

		assertThat(responseBody.get("status")).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
		assertThat(responseBody.get("error")).isEqualTo("Unauthorized");
		assertThat(responseBody.get("message")).isEqualTo("Unauthorized error message");
		assertThat(responseBody.get("path")).isEqualTo("/api/unauthorized");
	}
}
