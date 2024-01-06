package com.maxzamota.spring_sandbox.unit.repository.jpa;

import com.github.javafaker.Faker;
import com.maxzamota.spring_sandbox.util.generators.CustomerGenerator;
import com.maxzamota.spring_sandbox.unit.repository.jdbc.AbstractTestcontainersTest;
import com.maxzamota.spring_sandbox.repository.jpa.CustomerRepository;
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
    private CustomerRepository classUnderTest;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerByEmail() persistence layer method " +
            "passing existing customer e-mail")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JPA-001")
    @TmsLink("JPA-001")
    @Issue("JPA-001")
    void existsCustomerByEmail() {
        Allure.suite("JPA unit tests");
        // Arrange
        var customer = this.classUnderTest.save(new CustomerGenerator().generate());
        // Act
        var customerExists = this.classUnderTest.existsCustomerByEmail(customer.getEmail());
        // Assert
        assertThat(customerExists).isTrue();
    }

    @Test
    @DisplayName("Should return false when calling existsCustomerByEmail() persistence layer method " +
            "passing non-existing customer e-mail")
    @Tags({
            @Tag("negative")
    })
    @AllureId("JPA-002")
    @TmsLink("JPA-002")
    @Issue("JPA-002")
    void doesNotExistCustomerByEmail() {
        Allure.suite("JPA unit tests");
        // Arrange
        var email = this.faker.internet().safeEmailAddress();
        // Act
        var customerExists = this.classUnderTest.existsCustomerByEmail(email);
        // Assert
        assertThat(customerExists).isFalse();
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerById() persistence layer method " +
            "passing existing customer id")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JPA-003")
    @TmsLink("JPA-003")
    @Issue("JPA-003")
    void existsCustomerById() {
        Allure.suite("JPA unit tests");
        // Arrange
        var customer = this.classUnderTest.save(new CustomerGenerator().generate());
        // Act
        var customerExists = this.classUnderTest.existsCustomerById(customer.getId());
        // Assert
        assertThat(customerExists).isTrue();
    }

    @Test
    @DisplayName("Should return false when calling existsCustomerById() persistence layer method " +
            "passing non-existing customer id")
    @Tags({
            @Tag("negative")
    })
    @AllureId("JPA-004")
    @TmsLink("JPA-004")
    @Issue("JPA-004")
    void doesNotExistCustomerById() {
        Allure.suite("JPA unit tests");
        // Arrange
        Integer id = -1;
        // Act
        var customerExists = this.classUnderTest.existsCustomerById(id);
        // Assert
        assertThat(customerExists).isFalse();
    }

    @Test
    @DisplayName("Should return list of customers size of 1 when calling findCustomersByEmail()" +
            " persistence layer method passing existing customer's email")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JPA-005")
    @TmsLink("JPA-005")
    @Issue("JPA-005")
    void findCustomersByEmail() {
        Allure.suite("JPA unit tests");
        // Arrange
        var customer = this.classUnderTest.save(new CustomerGenerator().generate());
        // Act
        var customers = this.classUnderTest.findCustomersByEmail(customer.getEmail());
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
    @AllureId("JPA-006")
    @TmsLink("JPA-006")
    @Issue("JPA-006")
    void findCustomersByEmailNegative() {
        Allure.suite("JPA unit tests");
        // Arrange
        var email = this.faker.internet().safeEmailAddress();
        // Act
        var customers = this.classUnderTest.findCustomersByEmail(email);
        // Assert
        assertThat(customers).isEmpty();
    }
}