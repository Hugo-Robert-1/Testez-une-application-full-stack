package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	void testGettersAndSetters() {
		User user = new User();
		user.setId(1L);
		user.setEmail("test@email.com");
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setPassword("secret");
		user.setAdmin(true);

		assertEquals(1L, user.getId());
		assertEquals("test@email.com", user.getEmail());
		assertEquals("John", user.getFirstName());
		assertEquals("Doe", user.getLastName());
		assertEquals("secret", user.getPassword());
		assertTrue(user.isAdmin());
	}

	@Test
	void testSettersWithNullsForNullableFields() {
		User user = new User();
		user.setId(null);
		user.setCreatedAt(null);
		user.setUpdatedAt(null);

		assertNull(user.getId());
		assertNull(user.getCreatedAt());
		assertNull(user.getUpdatedAt());
	}

	@Test
	void testEqualsAndHashCode() {
		User user1 = new User();
		user1.setId(1L);
		user1.setEmail("test@email.com");

		User user2 = new User();
		user2.setId(1L);
		user2.setEmail("test@email.com");

		assertEquals(user1, user2);
		assertEquals(user1.hashCode(), user2.hashCode());
	}

	@Test
	void testEqualsAndHashCode2() {
		User user1 = User.builder().id(1L).email("a@a.com").firstName("A").lastName("B").password("p").admin(false)
				.build();
		User user2 = User.builder().id(1L).email("a@a.com").firstName("A").lastName("B").password("p").admin(false)
				.build();
		User user3 = User.builder().id(2L).email("b@b.com").firstName("C").lastName("D").password("q").admin(true)
				.build();

		assertEquals(user1, user2);
		assertEquals(user1.hashCode(), user2.hashCode());
		assertNotEquals(user1, user3);
		assertNotEquals(user1.hashCode(), user3.hashCode());
		assertNotEquals(user1, null);
		assertNotEquals(user1, "not a user");
		assertEquals(user1, user1);
	}

	@Test
	void testNoArgsConstructorAndSettersAndGetters() {
		User user = new User();
		user.setId(1L);
		user.setEmail("test@email.com");
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setPassword("secret");
		user.setAdmin(true);
		LocalDateTime now = LocalDateTime.now();
		user.setCreatedAt(now);
		user.setUpdatedAt(now);

		assertEquals(1L, user.getId());
		assertEquals("test@email.com", user.getEmail());
		assertEquals("John", user.getFirstName());
		assertEquals("Doe", user.getLastName());
		assertEquals("secret", user.getPassword());
		assertTrue(user.isAdmin());
		assertEquals(now, user.getCreatedAt());
		assertEquals(now, user.getUpdatedAt());
	}

	@Test
	void testAllArgsConstructor() {
		LocalDateTime now = LocalDateTime.now();
		User user = new User(1L, "test@email.com", "Doe", "John", "secret", true, now, now);

		assertEquals(1L, user.getId());
		assertEquals("test@email.com", user.getEmail());
		assertEquals("John", user.getFirstName());
		assertEquals("Doe", user.getLastName());
		assertEquals("secret", user.getPassword());
		assertTrue(user.isAdmin());
		assertEquals(now, user.getCreatedAt());
		assertEquals(now, user.getUpdatedAt());
	}

	@Test
	void testRequiredArgsConstructor() {
		User user = new User("test@email.com", "Doe", "John", "secret", true);
		assertEquals("test@email.com", user.getEmail());
		assertEquals("Doe", user.getLastName());
		assertEquals("John", user.getFirstName());
		assertEquals("secret", user.getPassword());
		assertTrue(user.isAdmin());
	}

	@Test
	void testToString() {
		User user = User.builder().id(1L).email("a@a.com").firstName("A").lastName("B").password("p").admin(false)
				.build();
		String str = user.toString();
		assertNotNull(str);
		assertTrue(str.contains("a@a.com"));
		assertTrue(str.contains("A"));
		assertTrue(str.contains("B"));
	}

	@Test
	void testSettersAndChainAccessors() {
		User user = new User();

		LocalDateTime now = LocalDateTime.now();

		user.setId(10L);
		user.setEmail("set@email.com");
		user.setFirstName("Setter");
		user.setLastName("Test");
		user.setPassword("setpass");
		user.setAdmin(true);
		user.setCreatedAt(now);
		user.setUpdatedAt(now);

		assertEquals(10L, user.getId());
		assertEquals("set@email.com", user.getEmail());
		assertEquals("Setter", user.getFirstName());
		assertEquals("Test", user.getLastName());
		assertEquals("setpass", user.getPassword());
		assertTrue(user.isAdmin());
		assertEquals(now, user.getCreatedAt());
		assertEquals(now, user.getUpdatedAt());

		User userChained = new User()
				.setId(3L)
				.setEmail("chain@email.com")
				.setFirstName("Chain")
				.setLastName("Accessors")
				.setPassword("chainpass")
				.setAdmin(true)
				.setCreatedAt(now)
				.setUpdatedAt(now);

		assertEquals(3L, userChained.getId());
		assertEquals("chain@email.com", userChained.getEmail());
		assertEquals("Chain", userChained.getFirstName());
		assertEquals("Accessors", userChained.getLastName());
		assertEquals("chainpass", userChained.getPassword());
		assertTrue(userChained.isAdmin());
		assertEquals(now, userChained.getCreatedAt());
		assertEquals(now, userChained.getUpdatedAt());

		// Vérifie que le chainage retourne bien l'objet courant
		assertSame(userChained, userChained.setFirstName("TestChain"));
		assertEquals("TestChain", userChained.getFirstName());
	}

	// BONUS

	@Test
	void requiredArgsConstructor_shouldSetAllNonNullFields() {
		User user = new User(
				"mail@mail.com",
				"Doe",
				"John",
				"pass",
				true);
		assertEquals("mail@mail.com", user.getEmail());
		assertEquals("Doe", user.getLastName());
		assertEquals("John", user.getFirstName());
		assertEquals("pass", user.getPassword());
		assertTrue(user.isAdmin());
	}

	@Test
	void requiredArgsConstructor_shouldThrowNPE_whenNonNullFieldIsNull() {
		assertThrows(NullPointerException.class, () -> new User(
				null, // email
				"Doe",
				"John",
				"pass",
				true));
		assertThrows(NullPointerException.class, () -> new User(
				"mail@mail.com",
				null, // lastName
				"John",
				"pass",
				true));
		assertThrows(NullPointerException.class, () -> new User(
				"mail@mail.com",
				"Doe",
				null, // firstName
				"pass",
				true));
		assertThrows(NullPointerException.class, () -> new User(
				"mail@mail.com",
				"Doe",
				"John",
				null, // password
				true));
	}

	@Test
	void allArgsConstructor_shouldSetAllFields() {
		LocalDateTime now = LocalDateTime.now();
		User user = new User(
				1L,
				"mail@mail.com",
				"Doe",
				"John",
				"pass",
				true,
				now,
				now);
		assertEquals(1L, user.getId());
		assertEquals("mail@mail.com", user.getEmail());
		assertEquals("Doe", user.getLastName());
		assertEquals("John", user.getFirstName());
		assertEquals("pass", user.getPassword());
		assertTrue(user.isAdmin());
		assertEquals(now, user.getCreatedAt());
		assertEquals(now, user.getUpdatedAt());
	}

	@Test
	void allArgsConstructor_shouldThrowNPE_whenNonNullFieldIsNull() {
		LocalDateTime now = LocalDateTime.now();
		assertThrows(NullPointerException.class, () -> new User(
				1L,
				null, // email
				"Doe",
				"John",
				"pass",
				true,
				now,
				now));
		assertThrows(NullPointerException.class, () -> new User(
				1L,
				"mail@mail.com",
				null, // lastName
				"John",
				"pass",
				true,
				now,
				now));
		assertThrows(NullPointerException.class, () -> new User(
				1L,
				"mail@mail.com",
				"Doe",
				null, // firstName
				"pass",
				true,
				now,
				now));
		assertThrows(NullPointerException.class, () -> new User(
				1L,
				"mail@mail.com",
				"Doe",
				"John",
				null, // password
				true,
				now,
				now));
	}

	@Test
	void allArgsConstructor_shouldAllowNullForNullableFields() {
		// id, createdAt, updatedAt can be null
		User user = new User(
				null,
				"mail@mail.com",
				"Doe",
				"John",
				"pass",
				true,
				null,
				null);
		assertNull(user.getId());
		assertNull(user.getCreatedAt());
		assertNull(user.getUpdatedAt());
	}

	@Test
	void testBuilder() {
		LocalDateTime now = LocalDateTime.now();
		User user = User.builder()
				.id(2L)
				.email("builder@email.com")
				.firstName("Jane")
				.lastName("Smith")
				.password("builderpass")
				.admin(false)
				.createdAt(now)
				.updatedAt(now)
				.build();

		assertEquals(2L, user.getId());
		assertEquals("builder@email.com", user.getEmail());
		assertEquals("Jane", user.getFirstName());
		assertEquals("Smith", user.getLastName());
		assertEquals("builderpass", user.getPassword());
		assertFalse(user.isAdmin());
		assertEquals(now, user.getCreatedAt());
		assertEquals(now, user.getUpdatedAt());
	}

	@Test
	void build_shouldAllowNullForNullableFields() {
		User user = User.builder()
				.email("builder@email.com")
				.lastName("Builder")
				.firstName("Test")
				.password("builderpass")
				.admin(false)
				.createdAt(null)
				.updatedAt(null)
				.id(null)
				.build();

		assertNull(user.getId());
		assertNull(user.getCreatedAt());
		assertNull(user.getUpdatedAt());
	}

	@Test
	void build_shouldThrowNPE_whenNonNullFieldIsNull() {
		// email
		assertThrows(NullPointerException.class, () -> User.builder()
				.email(null)
				.lastName("Builder")
				.firstName("Test")
				.password("builderpass")
				.admin(true)
				.build());

		// lastName
		assertThrows(NullPointerException.class, () -> User.builder()
				.email("builder@email.com")
				.lastName(null)
				.firstName("Test")
				.password("builderpass")
				.admin(true)
				.build());

		// firstName
		assertThrows(NullPointerException.class, () -> User.builder()
				.email("builder@email.com")
				.lastName("Builder")
				.firstName(null)
				.password("builderpass")
				.admin(true)
				.build());

		// password
		assertThrows(NullPointerException.class, () -> User.builder()
				.email("builder@email.com")
				.lastName("Builder")
				.firstName("Test")
				.password(null)
				.admin(true)
				.build());
	}

	@Test
	void builderChain_shouldReturnBuilderInstance() {
		User.UserBuilder builder = User.builder();
		assertSame(builder, builder.email("a@b.com"));
		assertSame(builder, builder.lastName("L"));
		assertSame(builder, builder.firstName("F"));
		assertSame(builder, builder.password("p"));
		assertSame(builder, builder.admin(false));
		assertSame(builder, builder.id(99L));
		assertSame(builder, builder.createdAt(null));
		assertSame(builder, builder.updatedAt(null));
	}

	@Test
	void build_toStringEqualsHashCode() {
		LocalDateTime now = LocalDateTime.now();
		User user1 = User.builder()
				.id(1L)
				.email("a@b.com")
				.lastName("L")
				.firstName("F")
				.password("p")
				.admin(false)
				.createdAt(now)
				.updatedAt(now)
				.build();

		User user2 = User.builder()
				.id(1L)
				.email("a@b.com")
				.lastName("L")
				.firstName("F")
				.password("p")
				.admin(false)
				.createdAt(now)
				.updatedAt(now)
				.build();

		assertEquals(user1, user2);
		assertEquals(user1.hashCode(), user2.hashCode());
		assertTrue(user1.toString().contains("a@b.com"));
	}

}
