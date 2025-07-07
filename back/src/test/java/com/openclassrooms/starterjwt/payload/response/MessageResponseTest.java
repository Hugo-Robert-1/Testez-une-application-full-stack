package com.openclassrooms.starterjwt.payload.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MessageResponseTest {

	@Test
	void setMessage_shouldSetMessage() {
		MessageResponse response = new MessageResponse("initial");

		response.setMessage("updated");

		assertThat(response.getMessage()).isEqualTo("updated");
	}
}
