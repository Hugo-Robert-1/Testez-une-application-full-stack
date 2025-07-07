package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserDetailsImplTest {

	@Test
	void builder_shouldBuildUserDetailsImpl() {
		UserDetailsImpl user = UserDetailsImpl.builder()
				.id(1L)
				.username("testuser")
				.firstName("John")
				.lastName("Doe")
				.admin(true)
				.password("secret")
				.build();

		assertThat(user.getId()).isEqualTo(1L);
		assertThat(user.getUsername()).isEqualTo("testuser");
		assertThat(user.getFirstName()).isEqualTo("John");
		assertThat(user.getLastName()).isEqualTo("Doe");
		assertThat(user.getAdmin()).isTrue();
		assertThat(user.getPassword()).isEqualTo("secret");
	}

	@Test
	void getAuthorities_shouldReturnEmptySet() {
		UserDetailsImpl user = UserDetailsImpl.builder().build();
		assertThat(user.getAuthorities()).isEmpty();
	}

	@Test
	void isAccountNonExpired_shouldReturnTrue() {
		UserDetailsImpl user = UserDetailsImpl.builder().build();
		assertThat(user.isAccountNonExpired()).isTrue();
	}

	@Test
	void isAccountNonLocked_shouldReturnTrue() {
		UserDetailsImpl user = UserDetailsImpl.builder().build();
		assertThat(user.isAccountNonLocked()).isTrue();
	}

	@Test
	void isCredentialsNonExpired_shouldReturnTrue() {
		UserDetailsImpl user = UserDetailsImpl.builder().build();
		assertThat(user.isCredentialsNonExpired()).isTrue();
	}

	@Test
	void isEnabled_shouldReturnTrue() {
		UserDetailsImpl user = UserDetailsImpl.builder().build();
		assertThat(user.isEnabled()).isTrue();
	}

	@Test
	void equals_shouldReturnTrueForSameId() {
		UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
		UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
		assertThat(user1).isEqualTo(user2);
	}

	@Test
	void equals_shouldReturnFalseForDifferentId() {
		UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
		UserDetailsImpl user2 = UserDetailsImpl.builder().id(2L).build();
		assertThat(user1).isNotEqualTo(user2);
	}

	@Test
	void equals_shouldReturnTrueWhenSameInstance() {
		UserDetailsImpl user = UserDetailsImpl.builder()
				.id(1L)
				.username("testuser")
				.build();

		// Teste l'égalité avec soi-même
		assertThat(user.equals(user)).isTrue();
	}

	@Test
	void equals_shouldReturnFalseWhenOtherIsNull() {
		UserDetailsImpl user = UserDetailsImpl.builder().id(1L).build();
		assertThat(user.equals(null)).isFalse();
	}

	@Test
	void equals_shouldReturnFalseWhenOtherIsDifferentClass() {
		UserDetailsImpl user = UserDetailsImpl.builder().id(1L).build();
		Object other = new Object();
		assertThat(user.equals(other)).isFalse();
	}
}