package com.maxzamota.spring_sandbox.integration;

import com.maxzamota.spring_sandbox.dto.CustomerDto;
import com.maxzamota.spring_sandbox.util.generators.CustomerGenerator;
import com.maxzamota.spring_sandbox.util.helpers.IntegrationTestHelpers;
import com.maxzamota.spring_sandbox.model.Customer;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Collection;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Execution(ExecutionMode.CONCURRENT)
@Epic("Customer management integration tests")
@Tags({
        @Tag("integration-test"),
        @Tag("customer")
})
@Severity(SeverityLevel.BLOCKER)
public class CustomerIT {
    @LocalServerPort
    private int port;
    @Autowired
    private ModelMapper mapper;
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @Test
    @DisplayName("Should be able to create a new customer and retrieve it using public customer API")
    @Description("This test checks that new customer can be created")
    @Issues(
            @Issue("CUST-001")
    )
    @AllureId("CUST-001")
    @TmsLink("CUST-001")
    @Tags({
            @Tag("positive")
    })
    void createNewCustomerAndVerifyExists() {
        Allure.suite("Customer management integration tests");

        // Arrange
        var step0 = "Init Web Test Client";
        logger.info(step0);
        step(step0);
        WebTestClient webClient = IntegrationTestHelpers.getWebTestClient(this.logger, this.port);

        var step1 = "Generate new Customer object";
        logger.info(step1);
        step(step1);
        var customer = new CustomerGenerator().generate();
        var customerDto = this.mapper.map(customer, CustomerDto.class);

        // Act
        var step2 = "Create generated customer using public Customer API";
        logger.info(step2);
        step(step2);
        Customer createdCustomer = IntegrationTestHelpers.postCustomer(webClient, customerDto);

        var step3 = "Get all customers using public Customer API";
        logger.info(step3);
        step(step3);
        Collection<Customer> allCustomers = IntegrationTestHelpers.getAllCustomers(webClient);

        var step4 = "Get customer by id using public Customer API";
        logger.info(step4);
        step(step4);
        Customer savedCustomer = IntegrationTestHelpers
                .getCustomerById(webClient, createdCustomer.getId());

        // Assert
        var step5 = "Verify all customers list contains created customer";
        logger.info(step5);
        step(step5);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(createdCustomer);

        var step6 = "Verify customer is returned by id";
        logger.info(step6);
        step(step6);
        assertThat(savedCustomer)
                .usingRecursiveComparison()
                .isEqualTo(createdCustomer);
    }

    @Test
    @DisplayName("Should be able to delete existing customer using public API")
    @Description("This test checks that existing customer can be deleted")
    @Issues(
            @Issue("CUST-002")
    )
    @AllureId("CUST-002")
    @TmsLink("CUST-002")
    @Tags({
            @Tag("positive")
    })
    void deleteCustomer() {
        Allure.suite("Customer management integration tests");

        // Arrange
        var step0 = "Init Web Test Client";
        logger.info(step0);
        step(step0);
        WebTestClient webClient = IntegrationTestHelpers.getWebTestClient(this.logger, this.port);

        var step1 = "Generate new customer object";
        logger.info(step1);
        step(step1);
        var customer = new CustomerGenerator().generate();
        var customerDto = this.mapper.map(customer, CustomerDto.class);

        // Act
        var step2 = "Create new customer using public API";
        logger.info(step2);
        step(step2);
        var createdCustomer = IntegrationTestHelpers.postCustomer(webClient, customerDto);

        var step3 = "Retrieve created customer by ID using public API";
        logger.info(step3);
        step(step3);
        var retrievedCustomer = IntegrationTestHelpers
                .getCustomerById(webClient, createdCustomer.getId());

        var step4 = "Delete created Customer";
        logger.info(step4);
        step(step4);
        var deleteResult = IntegrationTestHelpers
                .deleteCustomerById(webClient, createdCustomer.getId());

        var step5 = "Retrieve deleted customer";
        logger.info(step5);
        step(step5);
        var getDeletedCustomer = IntegrationTestHelpers
                .getCustomerByIdNotFound(webClient, createdCustomer.getId());

        // Assert
        var step6 = "Verify that retrieved customer before delete is as expected";
        logger.info(step6);
        step(step6);
        assertThat(retrievedCustomer)
                .usingRecursiveComparison()
                .isEqualTo(createdCustomer);

        var step7 = "Verify that get customer by id after delete returns not found";
        logger.info(step7);
        step(step7);
        assertThat(getDeletedCustomer)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue("error", "Not Found")
                .hasFieldOrPropertyWithValue(
                        "message",
                        "Customer with id={%s} not found!".formatted(createdCustomer.getId())
                );
    }

    @Test
    @DisplayName("Should be able to update existing customer using public API")
    @Description("This test checks that existing customer can be updated")
    @Issues(
            @Issue("CUST-003")
    )
    @AllureId("CUST-003")
    @TmsLink("CUST-003")
    @Tags({
            @Tag("positive")
    })
    void updateCustomer() {
        Allure.suite("Customer management integration tests");

        // Arrange
        var step0 = "Init Web Test Client";
        logger.info(step0);
        step(step0);
        WebTestClient webClient = IntegrationTestHelpers.getWebTestClient(this.logger, this.port);

        var step1 = "Generate new customer object";
        logger.info(step1);
        step(step1);
        var initialCustomerObj = new CustomerGenerator().generate();
        var customerDto = this.mapper.map(initialCustomerObj, CustomerDto.class);

        // Act
        var step2 = "Create new customer using public API";
        logger.info(step2);
        step(step2);
        var createdCustomer = IntegrationTestHelpers.postCustomer(webClient, customerDto);

        var step3 = "Generate updated customer object";
        logger.info(step3);
        step(step3);
        var updatedCustomerObj = new CustomerGenerator()
                .withId(createdCustomer.getId())
                .generate();

        var step4 = "Update customer using public API";
        logger.info(step4);
        step(step4);
        var updatedCustomer = IntegrationTestHelpers.putCustomer(webClient, updatedCustomerObj);

        var step5 = "Get customer by id using public API";
        logger.info(step5);
        step(step5);
        var retrievedCustomer = IntegrationTestHelpers
                .getCustomerById(webClient, createdCustomer.getId());

        // Assert
        var step6 = "Verify updated customer is as expected";
        logger.info(step6);
        step(step6);
        assertThat(updatedCustomer)
                .usingRecursiveComparison()
                .isEqualTo(updatedCustomerObj);
        assertThat(retrievedCustomer)
                .usingRecursiveComparison()
                .isEqualTo(updatedCustomerObj);
    }
}
