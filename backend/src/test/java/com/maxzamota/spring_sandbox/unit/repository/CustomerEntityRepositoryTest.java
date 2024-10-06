package com.maxzamota.spring_sandbox.unit.repository;

import com.github.javafaker.Faker;
import com.maxzamota.spring_sandbox.unit.AbstractTestcontainersTest;
import com.maxzamota.spring_sandbox.util.FakerProvider;
import com.maxzamota.spring_sandbox.util.generators.CustomerGenerator;
import com.maxzamota.spring_sandbox.repository.CustomerRepository;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Epic("Customer JPA unit tests")
@Execution(ExecutionMode.CONCURRENT)
@Tags({
        @Tag("unit-test"),
        @Tag("persistence-layer"),
        @Tag("jpa")
})
@Severity(SeverityLevel.BLOCKER)
class CustomerEntityRepositoryTest extends AbstractTestcontainersTest {

    @Autowired
    private CustomerRepository customerRepository;
    private static final Faker FAKER = FakerProvider.getInstance();
    private static final ThreadLocal<CustomerGenerator> GENERATOR = new ThreadLocal<>();

    @BeforeEach
    void setUp() {
        GENERATOR.set(new CustomerGenerator());
        Allure.suite("JPA unit tests");
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerByEmail() persistence layer method " +
            "passing existing customer e-mail")
    @Tags({
            @Tag("positive")
    })
    @AllureId("TBD")
    @TmsLink("TBD")
    @Issue("TBD")
    void existsCustomerByEmail() {
        // Arrange
        var customer = this.customerRepository.save(GENERATOR.get().buildNew().generate());
        // Act
        var customerExists = this.customerRepository.existsByEmail(customer.getEmail());
        // Assert
        assertThat(customerExists).isTrue();
    }

    @Test
    @DisplayName("Should return false when calling existsCustomerByEmail() persistence layer method " +
            "passing non-existing customer e-mail")
    @Tags({
            @Tag("negative")
    })
    @AllureId("TBD")
    @TmsLink("TBD")
    @Issue("TBD")
    void doesNotExistCustomerByEmail() {
        // Arrange
        var email = FAKER.internet().safeEmailAddress();
        // Act
        var customerExists = this.customerRepository.existsByEmail(email);
        // Assert
        assertThat(customerExists).isFalse();
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerById() persistence layer method " +
            "passing existing customer id")
    @Tags({
            @Tag("positive")
    })
    @AllureId("TBD")
    @TmsLink("TBD")
    @Issue("TBD")
    void existsCustomerById() {
        // Arrange
        var customer = this.customerRepository.save(GENERATOR.get().buildNew().generate());
        // Act
        var customerExists = this.customerRepository.existsById(customer.getId());
        // Assert
        assertThat(customerExists).isTrue();
    }

    @Test
    @DisplayName("Should return false when calling existsCustomerById() persistence layer method " +
            "passing non-existing customer id")
    @Tags({
            @Tag("negative")
    })
    @AllureId("TBD")
    @TmsLink("TBD")
    @Issue("TBD")
    void doesNotExistCustomerById() {
        // Arrange
        Integer id = -1;
        // Act
        var customerExists = this.customerRepository.existsById(id);
        // Assert
        assertThat(customerExists).isFalse();
    }

    @Test
    @DisplayName("Should return list of customers size of 1 when calling findCustomersByEmail()" +
            " persistence layer method passing existing customer's email")
    @Tags({
            @Tag("positive")
    })
    @AllureId("TBD")
    @TmsLink("TBD")
    @Issue("TBD")
    void findCustomersByEmail() {
        // Arrange
        var customer = this.customerRepository.save(GENERATOR.get().buildNew().generate());
        // Act
        var customers = this.customerRepository.findAllByEmail(customer.getEmail());
        // Assert
        assertThat(customers)
                .isNotEmpty()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(customer);
    }

    @Test
    @DisplayName("Should return empty list of customers when calling findCustomersByEmail()" +
            " persistence layer method passing non-existing customer's email")
    @Tags({
            @Tag("negative")
    })
    @AllureId("TBD")
    @TmsLink("TBD")
    @Issue("TBD")
    void findCustomersByEmailNegative() {
        // Arrange
        var email = FAKER.internet().safeEmailAddress();
        // Act
        var customers = this.customerRepository.findAllByEmail(email);
        // Assert
        assertThat(customers).isEmpty();
    }
}