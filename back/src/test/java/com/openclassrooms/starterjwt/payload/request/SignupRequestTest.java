package com.openclassrooms.starterjwt.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SignupRequestTest {

	@Test
	void hashCode_withNullField() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail(null);
		signupRequest1.setFirstName("John");
		signupRequest1.setLastName("Doe");
		signupRequest1.setPassword("password");

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setEmail(null);
		signupRequest2.setFirstName("John");
		signupRequest2.setLastName("Doe");
		signupRequest2.setPassword("password");

		// Both objects should have the same hashCode values
		assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode());
	}

	@Test
	void hashCode_similarObjectShouldEquals() {
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("test@example.com");
		signupRequest.setFirstName("John");
		signupRequest.setLastName("Doe");
		signupRequest.setPassword("password");

		int hashCode1 = signupRequest.hashCode();
		int hashCode2 = signupRequest.hashCode();

		assertEquals(hashCode1, hashCode2);
		assertEquals(hashCode1, hashCode1);
	}

	@Test
	void hashCode_shouldNotBeEqualsWithNullData() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail("test@example.com");
		signupRequest1.setFirstName(null);
		signupRequest1.setLastName("Doe");
		signupRequest1.setPassword("password");

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setEmail("test@example.com");
		signupRequest2.setFirstName("John");
		signupRequest2.setLastName("Doe");
		signupRequest2.setPassword("password");

		assertNotEquals(signupRequest1.hashCode(), signupRequest2.hashCode());
	}

	@Test
	void hashCode_compareSignupRequestWithOnlyNullFields() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail(null);
		signupRequest1.setFirstName(null);
		signupRequest1.setLastName(null);
		signupRequest1.setPassword(null);

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setEmail(null);
		signupRequest2.setFirstName(null);
		signupRequest2.setLastName(null);
		signupRequest2.setPassword(null);

		assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode());
	}

	@Test
	void equals_sameInstanceShouldBeEqual() {
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("test@example.com");
		signupRequest.setFirstName("John");
		signupRequest.setLastName("Doe");
		signupRequest.setPassword("password");

		assertEquals(signupRequest, signupRequest);
	}

	@Test
	void equals_withNullField() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail(null);
		signupRequest1.setFirstName("John");
		signupRequest1.setLastName("Doe");
		signupRequest1.setPassword("password");

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setEmail(null);
		signupRequest2.setFirstName("John");
		signupRequest2.setLastName("Doe");
		signupRequest2.setPassword("password");

		assertEquals(signupRequest1, signupRequest2);
	}

	@Test
	void equals_differentClass() {
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("test@example.com");
		signupRequest.setFirstName("John");
		signupRequest.setLastName("Doe");
		signupRequest.setPassword("password");

		Object otherObject = new Object();
		String differentTypeObject = "I'm a string";

		assertNotEquals(signupRequest, differentTypeObject);

		assertNotEquals(signupRequest, otherObject);
	}

	@Test
	void equals_withOneNullField() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail(null);
		signupRequest1.setFirstName("John");
		signupRequest1.setLastName("Doe");
		signupRequest1.setPassword("password");

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setEmail("test@example.com");
		signupRequest2.setFirstName("John");
		signupRequest2.setLastName("Doe");
		signupRequest2.setPassword("password");

		assertNotEquals(signupRequest1, signupRequest2);
	}

	@Test
	void equals_withDifferentFieldsValue() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail("test@example.com");
		signupRequest1.setFirstName("John");
		signupRequest1.setLastName("Doe");
		signupRequest1.setPassword("password");

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setEmail("different@example.com");
		signupRequest2.setFirstName("Jane");
		signupRequest2.setLastName("Smith");
		signupRequest2.setPassword("password456");

		assertNotEquals(signupRequest1, signupRequest2);
	}

	@Test
	void equals_withNullObject() {
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("test@example.com");
		signupRequest.setFirstName("John");
		signupRequest.setLastName("Doe");
		signupRequest.setPassword("password");

		assertFalse(signupRequest.equals(null));
	}

	@Test
	void equals_withDifferentPassword() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail("test@example.com");
		signupRequest1.setFirstName("John");
		signupRequest1.setLastName("Doe");
		signupRequest1.setPassword("password");

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setEmail("test@example.com");
		signupRequest2.setFirstName("John");
		signupRequest2.setLastName("Doe");
		signupRequest2.setPassword("differentPassword");

		assertNotEquals(signupRequest1, signupRequest2);
	}

	@Test
	void equals_withDifferentFirstName() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail("test@example.com");
		signupRequest1.setFirstName(null);
		signupRequest1.setLastName("Doe");
		signupRequest1.setPassword("password");

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setEmail("test@example.com");
		signupRequest2.setFirstName("John");
		signupRequest2.setLastName("Doe");
		signupRequest2.setPassword("password");

		assertNotEquals(signupRequest1, signupRequest2);
	}

	@Test
	void equals_withAllNullFields() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail(null);
		signupRequest1.setFirstName(null);
		signupRequest1.setLastName(null);
		signupRequest1.setPassword(null);

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setEmail(null);
		signupRequest2.setFirstName(null);
		signupRequest2.setLastName(null);
		signupRequest2.setPassword(null);

		assertEquals(signupRequest1, signupRequest2);
	}

	@Test
	void equals_withDifferentLastName() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail("test@email.com");
		signupRequest1.setFirstName("John");
		signupRequest1.setLastName(null);
		signupRequest1.setPassword("password");

		SignupRequest signupRequest2 = new SignupRequest();
		signupRequest2.setEmail("test@email.com");
		signupRequest2.setFirstName("John");
		signupRequest2.setLastName("Doe");
		signupRequest2.setPassword("password");

		assertNotEquals(signupRequest1, signupRequest2);
	}

	@Test
	void equals_withOneEmptyOneFilled() {
		SignupRequest signupRequest1 = new SignupRequest();
		signupRequest1.setEmail("test@email.com");
		signupRequest1.setFirstName("John");
		signupRequest1.setLastName("Doe");
		signupRequest1.setPassword("password");

		SignupRequest signupRequest2 = new SignupRequest();

		assertNotEquals(signupRequest1, signupRequest2);
	}
}