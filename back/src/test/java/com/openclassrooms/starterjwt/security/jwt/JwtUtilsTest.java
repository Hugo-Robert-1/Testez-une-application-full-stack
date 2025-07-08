package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtilsTest {

	@InjectMocks
	private JwtUtils jwtUtils;

	private final String jwtSecret = "testSecret";
	private final int jwtExpirationMs = 10000;
	private String validToken;
	private String expiredToken;

	@BeforeEach
	void setUp() {
		jwtUtils = new JwtUtils();
		ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
		ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);

		// Create a valid JWT token
		validToken = Jwts.builder()
				.setSubject("user")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(SignatureAlgorithm.HS512, "testSecret")
				.compact();

		// Create an expired JWT token
		expiredToken = Jwts.builder()
				.setSubject("user")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired
				.signWith(SignatureAlgorithm.HS512, "testSecret")
				.compact();
	}

	@Test
	void generateJwtToken_shouldReturnValidToken() {
		UserDetailsImpl userDetails = UserDetailsImpl.builder()
				.id(1L)
				.username("user")
				.firstName("John")
				.lastName("Doe")
				.admin(false)
				.password("password")
				.build();

		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

		String token = jwtUtils.generateJwtToken(authentication);

		assertThat(token).isNotNull();

		String subject = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		assertThat(subject).isEqualTo("user");
	}

	@Test
	void getUserNameFromJwtToken_shouldReturnUsername() {
		String token = Jwts.builder()
				.setSubject("user")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();

		String username = jwtUtils.getUserNameFromJwtToken(token);

		assertThat(username).isEqualTo("user");
	}

	@Test
	void validateJwtToken_shouldReturnTrue_whenTokenIsValid() {
		boolean isValid = jwtUtils.validateJwtToken(validToken);

		assertThat(isValid).isTrue();
	}

	@Test
	void validateJwtToken_InvalidSignature() {
		// Arrange: Modify the valid token to make it invalid
		String invalidToken = validToken + "invalidPart";

		assertFalse(jwtUtils.validateJwtToken(invalidToken), "Token with invalid signature should not be valid");
	}

	@Test
	void validateJwtToken_MalformedJwtException() {
		assertFalse(jwtUtils.validateJwtToken("wrong-token"), "Malformed token should not be valid");
	}

	@Test
	void validateJwtToken_ExpiredJwtException() {
		assertFalse(jwtUtils.validateJwtToken(expiredToken), "Expired token should not be valid");
	}

	@Test
	void validateJwtToken_shouldCatchUnsupportedJwtException() {
		String unsupportedToken = "eyJhbGciOiJub25lIn0.eyJzdWIiOiJ1c2VyIn0.";

		boolean isValid = jwtUtils.validateJwtToken(unsupportedToken);

		assertFalse(isValid); // Le token doit être rejeté
	}

	@Test
	void validateJwtToken_IllegalArgumentException() {
		assertFalse(jwtUtils.validateJwtToken(""), "Empty token should not be valid");
		assertFalse(jwtUtils.validateJwtToken(null), "Null token should not be valid");
	}
}
