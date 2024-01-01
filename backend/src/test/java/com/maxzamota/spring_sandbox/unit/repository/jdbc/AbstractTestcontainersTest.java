package com.maxzamota.spring_sandbox.unit.repository.jdbc;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestcontainersTest {

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("maxzamota-dao-unit-test")
                    .withUsername("mzamota")
                    .withPassword("password");

    /**
     * Runs Flyway migration scripts on testcontainers database
     */
    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl().replace("localhost", "127.0.0.1"),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        ).load();
        flyway.migrate();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    /**
     * Configure spring app properties to change datasource for tests' app context
     * @param registry
     */
    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                () -> postgreSQLContainer.getJdbcUrl().replace("localhost", "127.0.0.1")
        );
        registry.add(
                "spring.datasource.username",
                postgreSQLContainer::getUsername
        );
        registry.add(
                "spring.datasource.password",
                postgreSQLContainer::getPassword
        );
    }

    protected static DataSource getDataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword());
        return builder.build();
    }

    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}
