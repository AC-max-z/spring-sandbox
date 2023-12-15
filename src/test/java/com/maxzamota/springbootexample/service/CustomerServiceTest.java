package com.maxzamota.springbootexample.service;

import com.maxzamota.springbootexample.enums.CustomerSortType;
import com.maxzamota.springbootexample.exception.DuplicateResourceException;
import com.maxzamota.springbootexample.exception.ResourceNotFoundException;
import com.maxzamota.springbootexample.generators.CustomerGenerator;
import com.maxzamota.springbootexample.model.Customer;
import com.maxzamota.springbootexample.repository.jpa.CustomerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerService serviceUnderTest;
    AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository repository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        this.serviceUnderTest = new CustomerService(this.repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 69, 42, 100, 420})
    @DisplayName("Should invoke persistence layer method findAll() when application layer getAllCustomers() is called")
    @Tags({
            @Tag("unit-test"),
            @Tag("service-layer"),
            @Tag("positive"),
            @Tag("parameterized")
    })
    void getAllCustomers(int size) {
        // Arrange
        var mockedCustomers = new CustomerGenerator().buildList(size);
        when(this.repository.findAll()).thenReturn((List<Customer>) mockedCustomers);
        // Act
        var actualCustomers = this.serviceUnderTest.getAllCustomers();
        // Assert
        verify(this.repository)
                .findAll();
        assertThat(actualCustomers).isEqualTo(mockedCustomers);
    }


    @ParameterizedTest
    @NullSource
    @EnumSource(CustomerSortType.class)
    @DisplayName("Should return sorted list of customers when calling service layer method sortedCustomers()")
    @Tags({
            @Tag("unit-test"),
            @Tag("service-layer"),
            @Tag("positive"),
            @Tag("negative"),
            @Tag("parameterized")
    })
    void sortedCustomers(CustomerSortType sortType) {
        // Arrange
        var customers = new CustomerGenerator().buildList(69);
        // Act
        Collection<Customer> sortedCustomers = null;
        try {
            sortedCustomers = this.serviceUnderTest.sortedCustomers(customers, sortType);
        } catch (NullPointerException ignored) {
        }
        // Assert
        switch (sortType) {
            case BY_ID_ASC -> assertThat(sortedCustomers)
                    .isEqualTo(customers.stream()
                            .sorted(
                                    Comparator.comparingInt(Customer::getId)
                                            .thenComparing(Customer::getAge)
                                            .thenComparing(Customer::getName)
                            )
                            .toList()
                    );
            case BY_ID_DESC -> assertThat(sortedCustomers)
                    .isEqualTo(customers.stream()
                            .sorted(
                                    Comparator.comparingInt(Customer::getId).reversed()
                                            .thenComparing(Customer::getAge)
                                            .thenComparing(Customer::getName)
                            )
                            .toList()
                    );
            case BY_AGE_ASC -> assertThat(sortedCustomers)
                    .isEqualTo(customers.stream()
                            .sorted(
                                    Comparator.comparingInt(Customer::getAge)
                                            .thenComparing(Customer::getId)
                                            .thenComparing(Customer::getName)
                            )
                            .toList()
                    );
            case BY_AGE_DESC -> assertThat(sortedCustomers)
                    .isEqualTo(customers.stream()
                            .sorted(
                                    Comparator.comparingInt(Customer::getAge).reversed()
                                            .thenComparing(Customer::getId)
                                            .thenComparing(Customer::getName)
                            )
                            .toList()
                    );
            case BY_NAME_ASC -> assertThat(sortedCustomers)
                    .isEqualTo(customers.stream()
                            .sorted(
                                    Comparator.comparing(Customer::getName)
                                            .thenComparing(Customer::getId)
                                            .thenComparing(Customer::getAge)
                            )
                            .toList()
                    );
            case BY_NAME_DESC -> assertThat(sortedCustomers)
                    .isEqualTo(customers.stream()
                            .sorted(
                                    Comparator.comparing(Customer::getName).reversed()
                                            .thenComparing(Customer::getId)
                                            .thenComparing(Customer::getAge)
                            )
                            .toList()
                    );
            case null ->
                    assertThrows(NullPointerException.class, () -> this.serviceUnderTest.sortedCustomers(customers, sortType));
        }
    }

    @Test
    @DisplayName("Should invoke persistence layer method findById() passing same parameter that it took when calling service layer method getCustomerById()")
    @Tags({
            @Tag("unit-test"),
            @Tag("service-layer"),
            @Tag("positive")
    })
    void getCustomerById() {
        // Arrange
        int id = 69;
        var customer = new CustomerGenerator().build();
        when(this.repository.findById(id)).thenReturn(Optional.of(customer));
        // Act
        Customer actual = null;
        try {
            actual = this.serviceUnderTest.getCustomerById(id);
        } catch (ResourceNotFoundException ignored) {
        }
        // Assert
        verify(this.repository)
                .findById(id);
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    @DisplayName("Should throw exception when persistence layer method findById() returns empty Optional")
    @Tags({
            @Tag("unit-test"),
            @Tag("service-layer"),
            @Tag("negative")
    })
    void getCustomerByIdExceptionFlow() {
        // Arrange
        var id = 4444;
        when(this.repository.findById(id)).thenReturn(Optional.empty());
        // Act
        // Assert
        assertThatThrownBy(() -> this.serviceUnderTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id={%s} not found!".formatted(id));
    }

    @Test
    @DisplayName("Should call persistence layer save() method passing exact same entity as a parameter that it received when calling service layer save() method")
    @Tags({
            @Tag("unit-test"),
            @Tag("service-layer"),
            @Tag("positive")
    })
    void save() {
        // Arrange
        var customer = new CustomerGenerator().build();
        // Act
        this.serviceUnderTest.save(customer);
        // Assert
        verify(this.repository)
                .save(customer);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when persistence layer method existsCustomerByEmail() returns true")
    @Tags({
            @Tag("unit-test"),
            @Tag("service-layer"),
            @Tag("negative")
    })
    void saveExceptionFlow() {
        // Arrange
        var customer = new CustomerGenerator().build();
        when(this.repository.existsCustomerByEmail(customer.getEmail())).thenReturn(true);
        // Act
        // Assert
        assertThatThrownBy(() -> this.serviceUnderTest.save(customer))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Customer with email={%s} already exists!".formatted(customer.getEmail()));
        verify(this.repository, never()).save(any());
    }

    @Test
    @DisplayName("Should call persistence layer deleteById() method passing exact same id as a parameter that it received when calling service layer deleteById() method")
    @Tags({
            @Tag("unit-test"),
            @Tag("service-layer"),
            @Tag("positive")
    })
    void deleteById() {
        // Arrange
        int id = 420;
        // Act
        this.serviceUnderTest.deleteById(id);
        // Assert
        verify(this.repository)
                .deleteById(id);
    }

    @Test
    @DisplayName("Should call persistence layer save() method passing exact same entity as a parameter that it received when calling service layer updateById() method")
    @Tags({
            @Tag("unit-test"),
            @Tag("service-layer"),
            @Tag("positive")
    })
    void updateById() {
        // Arrange
        var id = 42;
        var customer = new CustomerGenerator().withId(id).build();

        when(this.repository.existsCustomerById(id)).thenReturn(true);
        when(this.repository.findCustomersByEmail(customer.getEmail())).thenReturn(new ArrayList<>());
        when(this.repository.save(customer)).thenReturn(customer);
        // Act
        Customer updatedCustomer = null;
        try {
            updatedCustomer = this.serviceUnderTest.updateById(customer);
        } catch (ResourceNotFoundException ignored) {
        }
        // Assert
        verify(this.repository)
                .existsCustomerById(id);
        verify(this.repository)
                .findById(id);
        verify(this.repository)
                .findCustomersByEmail(customer.getEmail());
        verify(this.repository)
                .save(customer);
        assertThat(updatedCustomer)
                .isEqualTo(customer);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updateById() service layer method and passing non-existent customer id")
    @Tags({
            @Tag("unit-test"),
            @Tag("service-layer"),
            @Tag("negative")
    })
    void updateByIdExceptionFlow1() {
        // Arrange
        var id = 42;
        var customer = new CustomerGenerator().withId(id).build();

        when(this.repository.existsCustomerById(id)).thenReturn(false);
        // Act
        // Assert
        assertThatThrownBy(() -> this.serviceUnderTest.updateById(customer))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id={%s} not found!".formatted(customer.getId()));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when updateById() service layer method and passing existing customer e-mail")
    @Tags({
            @Tag("unit-test"),
            @Tag("service-layer"),
            @Tag("negative")
    })
    void updateByIdExceptionFlow2() {
        // Arrange
        var id = 42;
        var id2 = 69;
        var updatedCustomer = new CustomerGenerator().withId(id).build();
        var anotherExistingCustomer = new CustomerGenerator()
                .withId(id2)
                .withEmail(updatedCustomer.getEmail())
                .build();

        when(this.repository.existsCustomerById(id)).thenReturn(true);
        when(this.repository.findCustomersByEmail(updatedCustomer.getEmail()))
                .thenReturn(List.of(updatedCustomer, anotherExistingCustomer));
        // Act
        // Assert
        assertThatThrownBy(() -> this.serviceUnderTest.updateById(updatedCustomer))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Customer with email={%s} already exists!"
                        .formatted(updatedCustomer.getEmail())
                );
    }
}