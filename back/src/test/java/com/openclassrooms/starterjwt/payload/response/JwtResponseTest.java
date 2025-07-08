package com.openclassrooms.starterjwt.payload.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class JwtResponseTest {
	@Test
	void jwtResponse_shouldSetAllAttributes() {
		JwtResponse jwtResponse = new JwtResponse("token", 1L, "user", "John", "Smith", false);

		jwtResponse.setToken("newToken");
		jwtResponse.setId(2L);
		jwtResponse.setType("Bearer");
		jwtResponse.setUsername("username");
		jwtResponse.setFirstName("Albert");
		jwtResponse.setLastName("Dobert");
		jwtResponse.setAdmin(true);

		assertThat(jwtResponse.getToken()).isEqualTo("newToken");
		assertThat(jwtResponse.getType()).isEqualTo("Bearer");
		assertThat(jwtResponse.getId()).isEqualTo(2L);
		assertThat(jwtResponse.getUsername()).isEqualTo("username");
		assertThat(jwtResponse.getFirstName()).isEqualTo("Albert");
		assertThat(jwtResponse.getLastName()).isEqualTo("Dobert");
		assertThat(jwtResponse.getAdmin()).isEqualTo(true);
	}
}
