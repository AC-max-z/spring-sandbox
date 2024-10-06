package com.maxzamota.spring_sandbox.integration;

import com.maxzamota.spring_sandbox.mappers.CustomerMapper;
import com.maxzamota.spring_sandbox.util.generators.CustomerGenerator;
import com.maxzamota.spring_sandbox.util.helpers.IntegrationTestHelpers;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static com.maxzamota.spring_sandbox.util.TestStep.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Execution(ExecutionMode.CONCURRENT)
@Epic("Customer management integration tests")
@Tags({
        @Tag("integration-test"),
        @Tag("customer")
})
@Severity(SeverityLevel.BLOCKER)
public class CustomerEntityIT {
    @LocalServerPort
    private int port;

    @Autowired
    private CustomerMapper mapper;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    private final ThreadLocal<Logger> LOGGER_THREAD_LOCAL = new ThreadLocal<>();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeEach
    void setUp() {
        Allure.suite("Customer management integration tests");
        LOGGER_THREAD_LOCAL.set(LoggerFactory.getLogger(this.getClass()));
    }

    @Test
    @DisplayName("Should be able to create a new customer and retrieve it using public customer API")
    @Description("This test checks that new customer can be created")
    @Issues(
            @Issue("TBD")
    )
    @AllureId("TBD")
    @TmsLink("TBD")
    @Tags({
            @Tag("positive")
    })
    void createNewCustomerAndVerifyExists() throws Exception {
        // Arrange
        var logger = LOGGER_THREAD_LOCAL.get();
        var webClient = step("Init Web Test Client", logger,
                () -> IntegrationTestHelpers.getWebTestClient(port));
        var customer = step("Generate new Customer object", logger,
                () -> new CustomerGenerator().generate());
        var customerDto = this.mapper.toDto(customer);
        // Act
        var createdCustomer = step("Create generated customer using public Customer API",
                logger, () -> IntegrationTestHelpers.postCustomer(webClient, customerDto));
        var allCustomers = step("Get all customers using public Customer API",
                logger, () -> IntegrationTestHelpers.getAllCustomers(webClient));
        var savedCustomer = step("Get customer by id using public Customer API",
                logger,
                () -> IntegrationTestHelpers.getCustomerById(webClient, createdCustomer.getId()));
        // Assert
        step("Verify all customers list contains created customer", logger,
                () -> assertThat(mapper.toDtoList(allCustomers.stream().toList()))
                        .usingRecursiveFieldByFieldElementComparator()
                        .contains(createdCustomer));
        step("Verify customer is returned by id", logger,
                () -> assertThat(savedCustomer)
                        .usingRecursiveComparison()
                        .isEqualTo(createdCustomer));
    }

    @Test
    @DisplayName("Should be able to delete existing customer using public API")
    @Description("This test checks that existing customer can be deleted")
    @Issues(
            @Issue("TBD")
    )
    @AllureId("TBD")
    @TmsLink("TBD")
    @Tags({
            @Tag("positive")
    })
    void deleteCustomer() throws Exception {
        // Arrange
        var logger = LOGGER_THREAD_LOCAL.get();
        var webClient = step("Init Web Test Client", logger,
                () -> IntegrationTestHelpers.getWebTestClient(port));
        var customer = step("Generate new customer object", logger,
                () -> new CustomerGenerator().generate());
        var customerDto = this.mapper.toDto(customer);
        // Act
        var createdCustomer = step("Create new customer using public API", logger,
                () -> IntegrationTestHelpers.postCustomer(webClient, customerDto));
        var retrievedCustomer = step("Retrieve created customer by ID using public API",
                logger,
                () -> IntegrationTestHelpers.getCustomerById(webClient, createdCustomer.getId()));
        step("Delete created Customer", logger,
                () -> IntegrationTestHelpers
                        .deleteCustomerById(webClient, createdCustomer.getId()));
        var getDeletedCustomer = step("Retrieve deleted customer", logger,
                () -> IntegrationTestHelpers
                        .getCustomerByIdNotFound(webClient, createdCustomer.getId()));
        // Assert
        step("Verify that retrieved customer before delete is as expected", logger,
                () -> assertThat(retrievedCustomer)
                        .usingRecursiveComparison()
                        .isEqualTo(createdCustomer));
        step("Verify that get customer by id after delete returns not found", logger,
                () -> assertThat(getDeletedCustomer)
                        .hasFieldOrPropertyWithValue("httpStatus", "NOT_FOUND")
                        .hasFieldOrPropertyWithValue(
                                "debugMessage",
                                "Customer with id={%s} not found!".formatted(createdCustomer.getId())
                        ));
    }

    @Test
    @DisplayName("Should be able to update existing customer using public API")
    @Description("This test checks that existing customer can be updated")
    @Issues(
            @Issue("TBD")
    )
    @AllureId("TBD")
    @TmsLink("TBD")
    @Tags({
            @Tag("positive")
    })
    void updateCustomer() throws Exception {
        // Arrange
        var logger = LOGGER_THREAD_LOCAL.get();
        var webClient = step("Init Web Test Client", logger,
                () -> IntegrationTestHelpers.getWebTestClient(port));
        var generator = new CustomerGenerator();
        var initialCustomerObj = step("Generate new customer object", logger,
                generator::generate);
        var customerDto = this.mapper.toDto(initialCustomerObj);
        // Act
        var createdCustomer = step("Create new customer using public API", logger,
                () -> IntegrationTestHelpers.postCustomer(webClient, customerDto));
        var updatedCustomerObj = step("Generate updated customer object", logger,
                () -> generator
                        .buildNew()
                        .withId(createdCustomer.getId())
                        .generate());
        var updatedCustomerDto = mapper.toDto(updatedCustomerObj);
        var updatedCustomer = step("Update customer using public API", logger,
                () -> IntegrationTestHelpers.putCustomer(webClient, updatedCustomerDto));
        var retrievedCustomer = step("Get customer by id using public API", logger,
                () -> IntegrationTestHelpers
                        .getCustomerById(webClient, createdCustomer.getId()));
        // Assert
        step("Verify updated customer is as expected", logger,
                () -> {
                    assertThat(updatedCustomer)
                            .usingRecursiveComparison()
                            .isEqualTo(updatedCustomerDto);
                    assertThat(retrievedCustomer)
                            .usingRecursiveComparison()
                            .isEqualTo(updatedCustomerDto);
                });

    }
}
