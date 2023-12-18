package com.maxzamota.springbootexample.unit.service;

import com.maxzamota.springbootexample.enums.CustomerSortType;
import com.maxzamota.springbootexample.exception.DuplicateResourceException;
import com.maxzamota.springbootexample.exception.ResourceNotFoundException;
import com.maxzamota.springbootexample.generators.CustomerGenerator;
import com.maxzamota.springbootexample.model.Customer;
import com.maxzamota.springbootexample.repository.jpa.CustomerRepository;
import com.maxzamota.springbootexample.service.CustomerService;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Execution(ExecutionMode.CONCURRENT)
@Epic("Customer service unit tests")
@Tags({
        @Tag("unit-test"),
        @Tag("service-layer")
})
@Severity(SeverityLevel.BLOCKER)
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
    @ValueSource(ints = {4, 69, 42, 100, 420})
    @DisplayName("Should invoke persistence layer method findAll() when application layer getAllCustomers() is called")
    @Tags({
            @Tag("positive"),
            @Tag("parameterized")
    })
    @AllureId("CUSTSERV-001")
    @TmsLink("CUSTSERV-001")
    @Issue("CUSTSERV-001")
    void getAllCustomers(int size) {
        Allure.suite("Customer service unit tests");

        // Arrange
        step("Generate list of customer objects");
        var mockedCustomers = new CustomerGenerator().buildList(size);
        step("Setup repository mock");
        when(this.repository.findAll()).thenReturn((List<Customer>) mockedCustomers);

        // Act
        step("Call getAllCustomers() service layer method");
        var actualCustomers = this.serviceUnderTest.getAllCustomers();

        // Assert
        step("Verify mock is called");
        verify(this.repository)
                .findAll();
        step("Verify mock returned expected list of customers");
        assertThat(actualCustomers).isEqualTo(mockedCustomers);
    }


    @ParameterizedTest
    @NullSource
    @EnumSource(CustomerSortType.class)
    @DisplayName("Should return sorted list of customers when calling service layer method sortedCustomers()")
    @Tags({
            @Tag("positive"),
            @Tag("negative"),
            @Tag("parameterized")
    })
    @AllureId("CUSTSERV-002")
    @TmsLink("CUSTSERV-002")
    @Issue("CUSTSERV-002")
    void sortedCustomers(CustomerSortType sortType) {
        Allure.suite("Customer service unit tests");

        // Arrange
        step("Generate list of customers");
        var customers = new CustomerGenerator().buildList(69);

        // Act
        step("Call service layer sortedCustomers() method");
        Collection<Customer> sortedCustomers = null;
        try {
            sortedCustomers = this.serviceUnderTest.sortedCustomers(customers, sortType);
        } catch (NullPointerException ignored) {
        }

        // Assert
        step("Verify that returned list is sorted as expected");
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
            @Tag("positive")
    })
    @AllureId("CUSTSERV-003")
    @TmsLink("CUSTSERV-003")
    @Issue("CUSTSERV-003")
    void getCustomerById() {
        Allure.suite("Customer service unit tests");

        // Arrange
        step("Generate new customer");
        int id = 69;
        var customer = new CustomerGenerator().generate();
        step("Setup repository mock");
        when(this.repository.findById(id)).thenReturn(Optional.of(customer));

        // Act
        step("Call getCustomerById() service layer method");
        Customer actual = null;
        try {
            actual = this.serviceUnderTest.getCustomerById(id);
        } catch (ResourceNotFoundException ignored) {
        }

        // Assert
        step("Verify mock is called");
        verify(this.repository)
                .findById(id);
        step("Verify mock returned expected customer object");
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    @DisplayName("Should throw exception when persistence layer method findById() returns empty Optional")
    @Tags({
            @Tag("negative")
    })
    @AllureId("CUSTSERV-004")
    @TmsLink("CUSTSERV-004")
    @Issue("CUSTSERV-004")
    void getCustomerByIdExceptionFlow() {
        Allure.suite("Customer service unit tests");

        // Arrange
        step("Setup repository mock");
        var id = 4444;
        when(this.repository.findById(id)).thenReturn(Optional.empty());

        // Act

        // Assert
        step("Verify that mock is called and exception is thrown by it");
        assertThatThrownBy(() -> this.serviceUnderTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id={%s} not found!".formatted(id));
        verify(this.repository).findById(id);
    }

    @Test
    @DisplayName("Should call persistence layer save() method passing exact same entity as a parameter that it received when calling service layer save() method")
    @Tags({
            @Tag("positive")
    })
    @AllureId("CUSTSERV-005")
    @TmsLink("CUSTSERV-005")
    @Issue("CUSTSERV-005")
    void save() {
        Allure.suite("Customer service unit tests");

        // Arrange
        step("Generate new customer object");
        var customer = new CustomerGenerator().generate();

        // Act
        step("Call service layer save() method");
        this.serviceUnderTest.save(customer);

        // Assert
        step("Verify mock is called");
        verify(this.repository)
                .save(customer);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when persistence layer method existsCustomerByEmail() returns true")
    @Tags({
            @Tag("negative")
    })
    @AllureId("CUSTSERV-006")
    @TmsLink("CUSTSERV-006")
    @Issue("CUSTSERV-006")
    void saveExceptionFlow() {
        Allure.suite("Customer service unit tests");

        // Arrange
        step("Generate new customer object");
        var customer = new CustomerGenerator().generate();
        step("Setup mock");
        when(this.repository.existsCustomerByEmail(customer.getEmail())).thenReturn(true);

        // Act

        // Assert
        step("Verify that mock is called and exception is thrown by it after invoking service layer save() method");
        assertThatThrownBy(() -> this.serviceUnderTest.save(customer))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Customer with email={%s} already exists!".formatted(customer.getEmail()));
        verify(this.repository, never()).save(any());
    }

    @Test
    @DisplayName("Should call persistence layer deleteById() method passing exact same id as a parameter that it received when calling service layer deleteById() method")
    @Tags({
            @Tag("positive")
    })
    @AllureId("CUSTSERV-007")
    @TmsLink("CUSTSERV-007")
    @Issue("CUSTSERV-007")
    void deleteById() {
        Allure.suite("Customer service unit tests");

        // Arrange
        int id = 420;

        // Act
        step("Call deleteById service layer method");
        this.serviceUnderTest.deleteById(id);

        // Assert
        step("Verify mock is called");
        verify(this.repository)
                .deleteById(id);
    }

    @Test
    @DisplayName("Should call persistence layer save() method passing exact same entity as a parameter that it received when calling service layer updateById() method")
    @Tags({
            @Tag("positive")
    })
    @AllureId("CUSTSERV-008")
    @TmsLink("CUSTSERV-008")
    @Issue("CUSTSERV-008")
    void updateById() {
        Allure.suite("Customer service unit tests");

        // Arrange
        step("Generate new customer object");
        var id = 42;
        var customer = new CustomerGenerator().withId(id).generate();

        step("Setup mock");
        when(this.repository.existsCustomerById(id)).thenReturn(true);
        when(this.repository.findCustomersByEmail(customer.getEmail())).thenReturn(new ArrayList<>());
        when(this.repository.save(customer)).thenReturn(customer);

        // Act
        step("Call updateById() service layer method");
        Customer updatedCustomer = null;
        try {
            updatedCustomer = this.serviceUnderTest.updateById(customer);
        } catch (ResourceNotFoundException ignored) {
        }

        // Assert
        step("Verify mock is called");
        verify(this.repository)
                .existsCustomerById(id);
        verify(this.repository)
                .findById(id);
        verify(this.repository)
                .findCustomersByEmail(customer.getEmail());
        verify(this.repository)
                .save(customer);
        step("Verify returned customer is as expected");
        assertThat(updatedCustomer)
                .isEqualTo(customer);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updateById() service layer method and passing non-existent customer id")
    @Tags({
            @Tag("negative")
    })
    @AllureId("CUSTSERV-009")
    @TmsLink("CUSTSERV-009")
    @Issue("CUSTSERV-009")
    void updateByIdExceptionCustomerNotFound() {
        Allure.suite("Customer service unit tests");

        // Arrange
        step("Generate new customer object");
        var id = 42;
        var customer = new CustomerGenerator().withId(id).generate();
        step("Setup mock");
        when(this.repository.existsCustomerById(id)).thenReturn(false);

        // Act

        // Assert
        step("Verify exception is thrown after calling updateById() service layer method");
        assertThatThrownBy(() -> this.serviceUnderTest.updateById(customer))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id={%s} not found!".formatted(customer.getId()));
        step("Verify repository mock save() method is never invoked");
        verify(this.repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when updateById() service layer method and passing existing customer e-mail")
    @Tags({
            @Tag("negative")
    })
    @AllureId("CUSTSERV-010")
    @TmsLink("CUSTSERV-010")
    @Issue("CUSTSERV-010")
    void updateByIdExceptionEmailTaken() {
        Allure.suite("Customer service unit tests");

        // Arrange
        step("Generate 2 new customer objects");
        var id = 42;
        var id2 = 69;
        var updatedCustomer = new CustomerGenerator().withId(id).generate();
        var anotherExistingCustomer = new CustomerGenerator()
                .withId(id2)
                .withEmail(updatedCustomer.getEmail())
                .generate();
        step("Setup mock");
        when(this.repository.existsCustomerById(id)).thenReturn(true);
        when(this.repository.findCustomersByEmail(updatedCustomer.getEmail()))
                .thenReturn(List.of(updatedCustomer, anotherExistingCustomer));

        // Act

        // Assert
        step("Verify that exception is thrown after calling service layer updateById() method");
        assertThatThrownBy(() -> this.serviceUnderTest.updateById(updatedCustomer))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Customer with email={%s} already exists!"
                        .formatted(updatedCustomer.getEmail())
                );
        step("Verify repository mock save() method is never called");
        verify(this.repository, never()).save(any());
    }
}