package com.maxzamota.spring_sandbox.unit.repository.jdbc;

import com.github.javafaker.Faker;
import com.maxzamota.spring_sandbox.util.generators.CustomerGenerator;
import com.maxzamota.spring_sandbox.model.Customer;
import com.maxzamota.spring_sandbox.repository.jdbc.CustomerJDBCDataAccessService;
import com.maxzamota.spring_sandbox.repository.mappers.CustomerRowMapper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Execution(ExecutionMode.SAME_THREAD)
@Epic("Customer repository JDBC unit tests")
@Tags({
        @Tag("unit-test"),
        @Tag("persistence-layer"),
        @Tag("jdbc")
})
@Severity(SeverityLevel.BLOCKER)
class CustomerDataAccessServiceTest extends AbstractTestcontainersTest {

    private static CustomerJDBCDataAccessService serviceUnderTest;
    private final static CustomerRowMapper customerRowMapper = new CustomerRowMapper();
    private final Faker faker = new Faker();

    @BeforeAll
    static void setUp() {
        serviceUnderTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    @DisplayName("Empty customers list is returned when calling findAll() persistence layer method on empty DB")
    @Tags({
            @Tag("negative")
    })
    @AllureId("JDBC-001")
    @TmsLink("JDBC-001")
    @Issue("JDBC-001")
    void findAllNegative() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        serviceUnderTest.clear();
        // Act
        var customers = serviceUnderTest.findAll();
        // Assert
        assertThat(customers).isEmpty();
    }

    @Test
    @DisplayName("Db should contain new customer after calling save() persistence layer method")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JDBC-002")
    @TmsLink("JDBC-002")
    @Issue("JDBC-002")
    void findAll() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        var savedCustomer = serviceUnderTest.save(new CustomerGenerator().generate());
        // Act
        Collection<Customer> actualCustomers = serviceUnderTest.findAll();
        // Assert
        assertThat(actualCustomers)
                .isNotEmpty()
                .usingRecursiveFieldByFieldElementComparator()
                .contains(savedCustomer);
    }

    @Test
    @DisplayName("Should be able to retrieve saved customer using findById() persistence layer method")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JDBC-003")
    @TmsLink("JDBC-003")
    @Issue("JDBC-003")
    void selectCustomerById() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        var savedCustomer = serviceUnderTest.save(new CustomerGenerator().generate());
        // Act
        Optional<Customer> actualCustomer = serviceUnderTest.findById(savedCustomer.getId());
        // Assert
        assertThat(actualCustomer)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedCustomer);
    }

    @Test
    @DisplayName("Should return empty object when calling findById() persistence layer method on non-existing customer id")
    @Tags({
            @Tag("negative")
    })
    @AllureId("JDBC-004")
    @TmsLink("JDBC-004")
    @Issue("JDBC-004")
    void selectCustomerByIdNonExistent() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        Integer id = -1;
        // Act
        Optional<Customer> actualCustomer = serviceUnderTest.findById(id);
        // Assert
        assertThat(actualCustomer).isNotPresent();
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerByEmail() persistence layer method passing existing customer e-mail")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JDBC-005")
    @TmsLink("JDBC-005")
    @Issue("JDBC-005")
    void existsCustomerByEmail() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        Customer customer = serviceUnderTest.save(new CustomerGenerator().generate());
        // Act
        var customerExistsByEmail = serviceUnderTest.existsCustomerByEmail(customer.getEmail());
        // Assert
        assertThat(customerExistsByEmail).isTrue();
    }

    @Test
    @DisplayName("Should return false when calling existsCustomerByEmail() persistence layer method passing non-existent customer e-mail")
    @Tags({
            @Tag("negative")
    })
    @AllureId("JDBC-006")
    @TmsLink("JDBC-006")
    @Issue("JDBC-006")
    void notExistCustomerByEmail() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        String nonExistentEmail = faker.name().firstName() + "." + faker.name().lastName()
                + "-" + UUID.randomUUID() + "@" + faker.internet().domainName();
        // Act
        Boolean customerExistsByEmail = serviceUnderTest.existsCustomerByEmail(nonExistentEmail);
        // Assert
        assertThat(customerExistsByEmail).isFalse();
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerById() persistence layer method passing existing customer id")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JDBC-007")
    @TmsLink("JDBC-007")
    @Issue("JDBC-007")
    void existsCustomerById() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        Customer customer = serviceUnderTest.save(new CustomerGenerator().generate());
        // Act
        Boolean customerExistsById = serviceUnderTest.existsCustomerById(customer.getId());
        // Assert
        assertThat(customerExistsById).isTrue();
    }

    @Test
    @DisplayName("Should return false when calling existsCustomerById() persistence layer method passing non-existent id")
    @Tags({
            @Tag("negative")
    })
    @AllureId("JDBC-008")
    @TmsLink("JDBC-008")
    @Issue("JDBC-008")
    void notExistCustomerById() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        Integer id = -2;
        // Act
        Boolean customerExistsById = serviceUnderTest.existsCustomerById(id);
        // Assert
        assertThat(customerExistsById).isFalse();
    }

    @Test
    @DisplayName("Should return collection of customers size of 1 when calling findCustomersByEmail() persistence layer method passing existing customer e-mail")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JDBC-009")
    @TmsLink("JDBC-009")
    @Issue("JDBC-009")
    void findCustomersByEmail() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        var customer = serviceUnderTest.save(new CustomerGenerator().generate());
        // Act
        var actualCustomers = serviceUnderTest.findCustomersByEmail(customer.getEmail());
        // Assert
        assertThat(actualCustomers)
                .usingRecursiveFieldByFieldElementComparator()
                .containsOnly(customer);
    }

    @Test
    @DisplayName("Should successfully create new customer when calling save() persistence layer method passing unique customer id and e-mail")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JDBC-010")
    @TmsLink("JDBC-010")
    @Issue("JDBC-010")
    void saveCreateSuccess() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        // Act
        var customer = serviceUnderTest.save(new CustomerGenerator().generate());
        // Assert
        var getCustomerById = serviceUnderTest.findById(customer.getId());
        assertThat(getCustomerById)
                .isNotNull()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(customer);
    }

    @Test
    @DisplayName("Should update existing customer when calling save() persistence layer method passing non-unique customer id and unique e-mail")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JDBC-011")
    @TmsLink("JDBC-011")
    @Issue("JDBC-011")
    void saveUpdateSuccess() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        var initialCustomer = serviceUnderTest.save(
                new CustomerGenerator()
                        .generate()
        );
        // Act
        var updatedCustomer = serviceUnderTest.save(
                new CustomerGenerator()
                        .withId(initialCustomer.getId())
                        .withName(initialCustomer.getName())
                        .withAge(initialCustomer.getAge())
                        .generate()
        );
        // Assert
        assertThat(updatedCustomer).satisfies(c -> {
            assertThat(c)
                    .usingRecursiveComparison()
                    .ignoringFields("email")
                    .isEqualTo(initialCustomer);
            assertThat(c.getEmail()).isNotEqualTo(initialCustomer.getEmail());
        });
    }

    @Test
    @DisplayName("Should throw exception when calling save() persistence layer method passing existing another customer e-mail")
    @Tags({
            @Tag("negative")
    })
    @AllureId("JDBC-012")
    @TmsLink("JDBC-012")
    @Issue("JDBC-012")
    void saveUpdateFail() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        var initialCustomer1 = serviceUnderTest.save(new CustomerGenerator().generate());
        // Act
        // Assert
        assertThatThrownBy(
                () -> serviceUnderTest.save(
                        new CustomerGenerator()
                                .withEmail(initialCustomer1.getEmail())
                                .generate()
                )
        ).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("Should throw exception when calling initial save() persistence layer method passing non-unique customer e-mail")
    @Tags({
            @Tag("negative")
    })
    @AllureId("JDBC-013")
    @TmsLink("JDBC-013")
    @Issue("JDBC-013")
    void saveCreateFailEmailExists() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        Function<Customer, Customer> saveCustomer = (customer) -> serviceUnderTest.save(customer);
        var customer1 = saveCustomer.apply(new CustomerGenerator().generate());
        // Act
        var customerObj2 = new CustomerGenerator().withEmail(customer1.getEmail()).generate();
        // Assert
        assertThatThrownBy(() -> saveCustomer.apply(customerObj2)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("Should delete existing customer when calling deleteById() persistence layer method")
    @Tags({
            @Tag("positive")
    })
    @AllureId("JDBC-014")
    @TmsLink("JDBC-014")
    @Issue("JDBC-014")
    void deleteById() {
        Allure.suite("Customer repository JDBC unit tests");
        // Arrange
        var customer = serviceUnderTest.save(new CustomerGenerator().generate());
        // Act
        serviceUnderTest.deleteById(customer.getId());
        var getCustomerByID = serviceUnderTest.findById(customer.getId());
        // Assert
        assertThat(getCustomerByID).isEmpty();
    }
}