package com.maxzamota.springbootexample.repository.jdbc;

import com.github.javafaker.Faker;
import com.maxzamota.springbootexample.AbstractTestcontainers;
import com.maxzamota.springbootexample.model.Customer;
import com.maxzamota.springbootexample.repository.mappers.CustomerRowMapper;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService serviceUnderTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        serviceUnderTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    @DisplayName("Db should contain new customer after calling save() persistence layer method")
    @Tags({@Tag("unit-test"), @Tag("persistence-layer"), @Tag("positive")})
    void findAll() {
        // Arrange
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "-" + UUID.randomUUID() + "@" + faker.internet().domainName();
        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                faker.number().numberBetween(0, 99)
        );
        serviceUnderTest.save(customer);
        // Act
        Collection<Customer> customers = serviceUnderTest.findAll();
        // Assert
        assertThat(customers).isNotEmpty();
    }

    @Test
    @DisplayName("Should be able to retrieve saved customer using findById() persistence layer method")
    @Tags({@Tag("unit-test"), @Tag("persistence-layer"), @Tag("positive")})
    void selectCustomerById() {
        // Arrange
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "-" + UUID.randomUUID() + "@" + faker.internet().domainName();
        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                faker.number().numberBetween(0, 99)
        );
        serviceUnderTest.save(customer);
        int id = serviceUnderTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // Act
        Optional<Customer> actualCustomer = serviceUnderTest.findById(id);
        // Assert
        assertThat(actualCustomer).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    @DisplayName("Should return empty object when calling findById() persistence layer method on non-existing customer id")
    @Tags({@Tag("unit-test"), @Tag("persistence-layer"), @Tag("negative")})
    void selectCustomerByIdNonExistent() {
        // Arrange
        Integer id = -1;
        // Act
        Optional<Customer> actualCustomer = serviceUnderTest.findById(id);
        // Assert
        assertThat(actualCustomer).isNotPresent();
    }

    @Test
    @DisplayName("Should always return exactly one customer with specified e-mail if it exists in database")
    @Tags({@Tag("unit-test"), @Tag("persistence-layer"), @Tag("positive")})
    void findCustomerByEmail() {
        // Arrange
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "-" + UUID.randomUUID() + "@" + faker.internet().domainName();
        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                faker.number().numberBetween(0, 99)
        );
        serviceUnderTest.save(customer);
        // Act
        Collection<Customer> actualCustomers = serviceUnderTest.findCustomersByEmail(email);
        // Assert
        assertThat(actualCustomers).isNotEmpty();
        assertThat(actualCustomers.stream().map(Customer::getEmail).toList()).contains(customer.getEmail());
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerByEmail() persistence layer method passing existing customer e-mail")
    @Tags({@Tag("unit-test"), @Tag("persistence-layer"), @Tag("positive")})
    void existsCustomerByEmail() {
        // Arrange
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "-" + UUID.randomUUID() + "@" + faker.internet().domainName();
        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                faker.number().numberBetween(0, 99)
        );
        serviceUnderTest.save(customer);
        // Act
        // Assert
    }
}