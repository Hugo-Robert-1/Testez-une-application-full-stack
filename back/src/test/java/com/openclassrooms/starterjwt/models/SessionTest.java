package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;

class SessionTest {

	@Test
	void testGettersAndSetters() {
		Session session = new Session();
		session.setId(1L);
		session.setName("Yoga");
		session.setDescription("Morning yoga");
		Date date = new Date();
		LocalDateTime now = LocalDateTime.now();
		session.setDate(date);
		session.setTeacher(new Teacher());
		session.setUsers(new ArrayList<>());
		session.setCreatedAt(now);
		session.setUpdatedAt(now);

		assertEquals(1L, session.getId());
		assertEquals("Yoga", session.getName());
		assertEquals("Morning yoga", session.getDescription());
		assertEquals(date, session.getDate());
		assertNotNull(session.getTeacher());
		assertNotNull(session.getUsers());
		assertEquals(now, session.getCreatedAt());
		assertEquals(now, session.getUpdatedAt());
	}

	@Test
	void testEqualsAndHashCode() {
		Session s1 = new Session();
		s1.setId(1L);

		Session s2 = new Session();
		s2.setId(1L);

		assertEquals(s1, s2);
		assertEquals(s1.hashCode(), s2.hashCode());
	}

	@Test
	void testEquals_reflexive() {
		Session s = Session.builder().id(1L).build();
		assertEquals(s, s);
	}

	@Test
	void testEquals_null() {
		Session s = Session.builder().id(1L).build();
		assertNotEquals(s, null);
	}

	@Test
	void testEquals_otherType() {
		Session s = Session.builder().id(1L).build();
		assertNotEquals(s, "not a session");
	}

	@Test
	void testEquals_sameId() {
		Session s1 = Session.builder().id(1L).build();
		Session s2 = Session.builder().id(1L).build();
		assertEquals(s1, s2);
		assertEquals(s2, s1);
		assertEquals(s1.hashCode(), s2.hashCode());
	}

	@Test
	void testEquals_differentId() {
		Session s1 = Session.builder().id(1L).build();
		Session s2 = Session.builder().id(2L).build();
		assertNotEquals(s1, s2);
		assertNotEquals(s2, s1);
	}

	@Test
	void testEquals_bothNullId() {
		Session s1 = Session.builder().id(null).build();
		Session s2 = Session.builder().id(null).build();
		assertEquals(s1, s2);
		assertEquals(s1.hashCode(), s2.hashCode());
	}

	@Test
	void testEquals_oneNullId() {
		Session s1 = Session.builder().id(null).build();
		Session s2 = Session.builder().id(2L).build();
		assertNotEquals(s1, s2);
		assertNotEquals(s2, s1);
	}
}
