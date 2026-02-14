package com.pizza.delivery.service;

import com.pizza.models.OrderReadyEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@DisplayName("OrderReadyEvent JSON Mapping Tests")
class OrderReadyEventJsonTest {

    @Autowired
    private JacksonTester<OrderReadyEvent> json;

    @Test
    @DisplayName("Should correctly deserialize received RabbitMQ JSON into OrderReadyEvent")
    void shouldDeserializeReceivedJson() throws Exception {
        // Given
        String jsonContent = """
                {
                    "orderId": "order-123",
                    "pizza": "Margherita",
                    "quantity": 2,
                    "address": "Musterstrasse 123, 8000 Zurich",
                    "customerName": "Max Mustermann",
                    "preparedAt": "2026-01-16T10:00:00"
                }
                """;

        // When
        OrderReadyEvent event = json.parseObject(jsonContent);

        // Then
        assertThat(event.getOrderId()).isEqualTo("order-123");
        assertThat(event.getPizza()).isEqualTo("Margherita");
        assertThat(event.getQuantity()).isEqualTo(2);
        assertThat(event.getAddress()).isEqualTo("Musterstrasse 123, 8000 Zurich");
        assertThat(event.getCustomerName()).isEqualTo("Max Mustermann");
        assertThat(event.getPreparedAt()).isEqualTo(LocalDateTime.of(2026, 1, 16, 10, 0, 0));
    }
}
