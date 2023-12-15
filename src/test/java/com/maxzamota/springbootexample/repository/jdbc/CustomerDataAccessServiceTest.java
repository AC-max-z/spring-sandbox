package com.maxzamota.springbootexample.repository.jdbc;

import com.github.javafaker.Faker;
import com.maxzamota.springbootexample.generators.CustomerGenerator;
import com.maxzamota.springbootexample.model.Customer;
import com.maxzamota.springbootexample.repository.mappers.CustomerRowMapper;
import org.junit.jupiter.api.*;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomerDataAccessServiceTest extends AbstractTestcontainersTest {

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
    @DisplayName("Empty customers list is returned when calling findAll() persistence layer method on empty DB")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("negative"),
            @Tag("jdbc")
    })
    void findAllNegative() {
        // Arrange
        this.serviceUnderTest.clear();
        // Act
        var customers = this.serviceUnderTest.findAll();
        // Assert
        assertThat(customers).isEmpty();
    }

    @Test
    @DisplayName("Db should contain new customer after calling save() persistence layer method")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jdbc")
    })
    void findAll() {
        // Arrange
        var savedCustomer = this.serviceUnderTest.save(new CustomerGenerator().build());
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
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jdbc")
    })
    void selectCustomerById() {
        // Arrange
        var savedCustomer = this.serviceUnderTest.save(new CustomerGenerator().build());
        // Act
        Optional<Customer> actualCustomer = this.serviceUnderTest.findById(savedCustomer.getId());
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
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("negative"),
            @Tag("jdbc")
    })
    void selectCustomerByIdNonExistent() {
        // Arrange
        Integer id = -1;
        // Act
        Optional<Customer> actualCustomer = this.serviceUnderTest.findById(id);
        // Assert
        assertThat(actualCustomer).isNotPresent();
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerByEmail() persistence layer method passing existing customer e-mail")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jdbc")
    })
    void existsCustomerByEmail() {
        // Arrange
        Customer customer = this.serviceUnderTest.save(new CustomerGenerator().build());
        // Act
        var customerExistsByEmail = this.serviceUnderTest.existsCustomerByEmail(customer.getEmail());
        // Assert
        assertThat(customerExistsByEmail).isTrue();
    }

    @Test
    @DisplayName("Should return false when calling existsCustomerByEmail() persistence layer method passing non-existent customer e-mail")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("negative"),
            @Tag("jdbc")
    })
    void notExistCustomerByEmail() {
        // Arrange
        String nonExistentEmail = faker.name().firstName() + "." + faker.name().lastName()
                + "-" + UUID.randomUUID() + "@" + faker.internet().domainName();
        // Act
        Boolean customerExistsByEmail = this.serviceUnderTest.existsCustomerByEmail(nonExistentEmail);
        // Assert
        assertThat(customerExistsByEmail).isFalse();
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerById() persistence layer method passing existing customer id")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jdbc")
    })
    void existsCustomerById() {
        // Arrange
        Customer customer = this.serviceUnderTest.save(new CustomerGenerator().build());
        // Act
        Boolean customerExistsById = this.serviceUnderTest.existsCustomerById(customer.getId());
        // Assert
        assertThat(customerExistsById).isTrue();
    }

    @Test
    @DisplayName("Should return false when calling existsCustomerById() persistence layer method passing non-existent id")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("negative"),
            @Tag("jdbc")
    })
    void notExistCustomerById() {
        // Arrange
        Integer id = -2;
        // Act
        Boolean customerExistsById = this.serviceUnderTest.existsCustomerById(id);
        // Assert
        assertThat(customerExistsById).isFalse();
    }

    @Test
    @DisplayName("Should return collection of customers size of 1 when calling findCustomersByEmail() persistence layer method passing existing customer e-mail")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jdbc")
    })
    void findCustomersByEmail() {
        // Arrange
        var customer = this.serviceUnderTest.save(new CustomerGenerator().build());
        // Act
        var actualCustomers = this.serviceUnderTest.findCustomersByEmail(customer.getEmail());
        // Assert
        assertThat(actualCustomers)
                .usingRecursiveFieldByFieldElementComparator()
                .containsOnly(customer);
    }

    @Test
    @DisplayName("Should successfully create new customer when calling save() persistence layer method passing unique customer id and e-mail")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jdbc")
    })
    void saveCreateSuccess() {
        // Arrange
        // Act
        var customer = this.serviceUnderTest.save(new CustomerGenerator().build());
        // Assert
        var getCustomerById = this.serviceUnderTest.findById(customer.getId());
        assertThat(getCustomerById)
                .isNotNull()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(customer);
    }

    @Test
    @DisplayName("Should update existing customer when calling save() persistence layer method passing non-unique customer id and unique e-mail")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jdbc")
    })
    void saveUpdateSuccess() {
        // Arrange
        var initialCustomer = this.serviceUnderTest.save(
                new CustomerGenerator()
                        .build()
        );
        // Act
        var updatedCustomer = this.serviceUnderTest.save(
                new CustomerGenerator()
                        .withId(initialCustomer.getId())
                        .withName(initialCustomer.getName())
                        .withAge(initialCustomer.getAge())
                        .build()
        );
        // Assert
        assertThat(updatedCustomer).satisfies(c -> {
            assertThat(c)
                    .usingRecursiveComparison()
                    .ignoringFields("email")
                    .isEqualTo(initialCustomer);
            assertThat(c.getEmail()).isNotEqualTo(initialCustomer.getEmail() );
        });
    }

    @Test
    @DisplayName("Should throw exception when calling save() persistence layer method passing existing another customer e-mail")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("negative"),
            @Tag("jdbc")
    })
    void saveUpdateFail() {
        // Arrange
        var initialCustomer1 = this.serviceUnderTest.save(new CustomerGenerator().build());
        // Act
        // Assert
        assertThatThrownBy(
                () -> this.serviceUnderTest.save(
                        new CustomerGenerator()
                                .withEmail(initialCustomer1.getEmail())
                                .build()
                )
        ).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("Should throw exception when calling initial save() persistence layer method passing non-unique customer e-mail")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("negative"),
            @Tag("jdbc")
    })
    void saveCreateFailEmailExists() {
        // Arrange
        Function<Customer, Customer> saveCustomer = (customer) -> this.serviceUnderTest.save(customer);
        var customer1 = saveCustomer.apply(new CustomerGenerator().build());
        // Act
        var customerObj2 = new CustomerGenerator().withEmail(customer1.getEmail()).build();
        // Assert
        assertThatThrownBy(() -> saveCustomer.apply(customerObj2)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("Should delete existing customer when calling deleteById() persistence layer method")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jdbc")
    })
    void deleteById() {
        // Arrange
        var customer = this.serviceUnderTest.save(new CustomerGenerator().build());
        // Act
        this.serviceUnderTest.deleteById(customer.getId());
        var getCustomerByID = this.serviceUnderTest.findById(customer.getId());
        // Assert
        assertThat(getCustomerByID).isEmpty();
    }
}