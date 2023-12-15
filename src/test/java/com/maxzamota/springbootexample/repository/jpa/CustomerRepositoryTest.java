package com.maxzamota.springbootexample.repository.jpa;

import com.github.javafaker.Faker;
import com.maxzamota.springbootexample.generators.CustomerGenerator;
import com.maxzamota.springbootexample.repository.jdbc.AbstractTestcontainersTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainersTest {

    @Autowired
    private CustomerRepository classUnderTest;
    @Autowired
    private ApplicationContext ctx;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        System.out.println("Loaded app context with number of beans: " + ctx.getBeanDefinitionCount());
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerByEmail() persistence layer method " +
            "passing existing customer e-mail")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jpa")
    })
    void existsCustomerByEmail() {
        // Arrange
        var customer = this.classUnderTest.save(new CustomerGenerator().build());
        // Act
        var customerExists = this.classUnderTest.existsCustomerByEmail(customer.getEmail());
        // Assert
        assertThat(customerExists).isTrue();
    }

    @Test
    @DisplayName("Should return false when calling existsCustomerByEmail() persistence layer method " +
            "passing non-existing customer e-mail")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("negative"),
            @Tag("jpa")
    })
    void doesNotExistCustomerByEmail() {
        // Arrange
        var email = faker.internet().safeEmailAddress();
        // Act
        var customerExists = this.classUnderTest.existsCustomerByEmail(email);
        // Assert
        assertThat(customerExists).isFalse();
    }

    @Test
    @DisplayName("Should return true when calling existsCustomerById() persistence layer method " +
            "passing existing customer id")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jpa")
    })
    void existsCustomerById() {
        // Arrange
        var customer = this.classUnderTest.save(new CustomerGenerator().build());
        // Act
        var customerExists = this.classUnderTest.existsCustomerById(customer.getId());
        // Assert
        assertThat(customerExists).isTrue();
    }

    @Test
    @DisplayName("Should return false when calling existsCustomerById() persistence layer method " +
            "passing non-existing customer id")
    @Tags({
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("negative"),
            @Tag("jpa")
    })
    void doesNotExistCustomerById() {
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
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("positive"),
            @Tag("jpa")
    })
    void findCustomersByEmail() {
        // Arrange
        var customer = this.classUnderTest.save(new CustomerGenerator().build());
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
            @Tag("unit-test"),
            @Tag("persistence-layer"),
            @Tag("negative"),
            @Tag("jpa")
    })
    void findCustomersByEmailNegative() {
        // Arrange
        var email = faker.internet().safeEmailAddress();
        // Act
        var customers = this.classUnderTest.findCustomersByEmail(email);
        // Assert
        assertThat(customers).isEmpty();
    }
}