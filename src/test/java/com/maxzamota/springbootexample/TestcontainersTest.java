package com.maxzamota.springbootexample;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class TestcontainersTest extends AbstractTestcontainers {

    @Test
    void canStartPostgresDB() {
        // Arrange
        // Act
        // Assert
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
