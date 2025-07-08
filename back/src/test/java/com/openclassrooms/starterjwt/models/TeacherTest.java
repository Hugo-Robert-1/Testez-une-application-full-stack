package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class TeacherTest {

	@Test
	void testGettersAndSetters() {
		Teacher teacher = new Teacher();
		teacher.setId(1L);
		teacher.setFirstName("Jane");
		teacher.setLastName("Smith");

		assertEquals(1L, teacher.getId());
		assertEquals("Jane", teacher.getFirstName());
		assertEquals("Smith", teacher.getLastName());
	}

	@Test
	void testEqualsAndHashCode() {
		Teacher t1 = new Teacher();
		t1.setId(1L);

		Teacher t2 = new Teacher();
		t2.setId(1L);

		assertEquals(t1, t2);
		assertEquals(t1.hashCode(), t2.hashCode());
	}

	@Test
	void testToString() {
		Teacher teacher = Teacher.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();

		String str = teacher.toString();
		assertNotNull(str);
		assertTrue(str.contains("John"));
		assertTrue(str.contains("Doe"));
		assertTrue(str.contains("1"));
	}

	@Test
	void testEqualsAndHashCode_sameId() {
		Teacher t1 = Teacher.builder().id(1L).firstName("A").lastName("B").build();
		Teacher t2 = Teacher.builder().id(1L).firstName("C").lastName("D").build();

		assertEquals(t1, t2);
		assertEquals(t1.hashCode(), t2.hashCode());
	}

	@Test
	void testEqualsAndHashCode_differentId() {
		Teacher t1 = Teacher.builder().id(1L).firstName("A").lastName("B").build();
		Teacher t2 = Teacher.builder().id(2L).firstName("A").lastName("B").build();

		assertNotEquals(t1, t2);
		assertNotEquals(t1.hashCode(), t2.hashCode());
	}

	@Test
	void testEquals_nullAndOtherType() {
		Teacher t1 = Teacher.builder().id(1L).firstName("A").lastName("B").build();

		assertNotEquals(t1, null);
		assertNotEquals(t1, "not a teacher");
	}

	@Test
	void testEquals_reflexive() {
		Teacher t1 = Teacher.builder().id(1L).firstName("A").lastName("B").build();
		assertEquals(t1, t1);
	}

	@Test
	void testEquals_nullId() {
		Teacher t1 = Teacher.builder().id(null).firstName("A").lastName("B").build();
		Teacher t2 = Teacher.builder().id(null).firstName("A").lastName("B").build();

		assertEquals(t1, t2);
		assertEquals(t1.hashCode(), t2.hashCode());
	}

	@Test
	void testEquals_oneNullId() {
		Teacher t1 = Teacher.builder().id(null).firstName("A").lastName("B").build();
		Teacher t2 = Teacher.builder().id(2L).firstName("A").lastName("B").build();

		assertNotEquals(t1, t2);
		assertNotEquals(t2, t1);
	}

	@Test
	void testTeacherBuilderToString() {
		LocalDateTime now = LocalDateTime.now();
		Teacher.TeacherBuilder builder = Teacher.builder()
				.id(42L)
				.firstName("Alice")
				.lastName("Smith")
				.createdAt(now)
				.updatedAt(now);

		String builderString = builder.toString();

		assertNotNull(builderString);
		assertTrue(builderString.contains("42"));
		assertTrue(builderString.contains("Alice"));
		assertTrue(builderString.contains("Smith"));
		assertTrue(builderString.contains(now.toString()));
	}
}