package com.maxzamota.springbootexample.unit.repository.jdbc;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class TestcontainersTest extends AbstractTestcontainersTest {

    @Test
    void canStartPostgresDB() {
        // Arrange
        // Act
        // Assert
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
