package tests;

import generators.CustomerGenerator;
import helpers.CustomerHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springsandbox.domain.Customer;
import org.springsandbox.enums.DriverType;
import org.springsandbox.pages.IndexPage;
import utils.TestDataProvider;

import java.util.*;
import java.util.stream.Stream;

import static utils.TestStep.step;

@Epic("Customer UI tests")
@Tag("customer")
public class CustomerTest extends BaseTest {
    private static final CustomerGenerator GENERATOR = new CustomerGenerator();

    @ParameterizedTest(name = "{displayName} (driver type: {0})")
    @MethodSource("provideDriverTypesAndCustomers")
    @DisplayName("Should display new customer after creating one")
    @Description("This test creates new customer, checks if it is present on page, then deletes it")
    @Tags({
            @Tag("positive"),
            @Tag("regression"),
            @Tag("acceptance")
    })
    @Severity(SeverityLevel.BLOCKER)
    public void shouldDisplayNewCustomerAfterCreate(
            DriverType driverType,
            Customer customer,
            Customer updatedCustomer
    ) throws Exception {
        // Arrange
        var logger = LOGGER_THREAD_LOCAL.get();
        var driver = step("Create driver instance", logger, () -> setupDriver(driverType));
        // Act
        var indexPage = new IndexPage(driver);
        CustomerHelper.createNewCustomer(driver, indexPage, customer, logger);
        // Assert
        var createdCustomerCard = CustomerHelper
                .verifyPageContainsCustomer(indexPage, customer, logger);
        // Cleanup
        CustomerHelper.deleteCustomer(indexPage, customer, createdCustomerCard, logger);
    }

    @ParameterizedTest(name = "{displayName} (driver type: {0})")
    @MethodSource("provideCustomerTestDataFromYml")
    @DisplayName("Should display updated customer data after editing one")
    @Description("This test creates new customer, then edits it, checks if customer was updated and then deletes it")
    @Tags({
            @Tag("positive"),
            @Tag("regression"),
            @Tag("acceptance")
    })
    @Severity(SeverityLevel.BLOCKER)
    public void shouldDisplayUpdatedCustomerAfterEdit(
            DriverType driverType,
            Customer initialCustomer,
            Customer updatedCustomer
    ) throws Exception {
        // Arrange
        var logger = LOGGER_THREAD_LOCAL.get();
        var driver = step("Create driver instance", logger, () -> setupDriver(driverType));
        // Act
        var indexPage = new IndexPage(driver);
        CustomerHelper.createNewCustomer(driver, indexPage, initialCustomer, logger);
        CustomerHelper.verifyPageContainsCustomer(indexPage, initialCustomer, logger);
        CustomerHelper.editCustomer(driver, indexPage, initialCustomer, updatedCustomer, logger);
        // Assert
        var updatedCustomerCard = CustomerHelper
                .verifyPageContainsCustomer(indexPage, updatedCustomer, logger);
        // Cleanup
        CustomerHelper.deleteCustomer(indexPage, updatedCustomer, updatedCustomerCard, logger);
    }

    private static Stream<Arguments> provideDriverTypesAndCustomers() {
        return Stream.of(
                Arguments.of(DriverType.CHROME_REMOTE, GENERATOR.generate(), GENERATOR.generate()),
                Arguments.of(DriverType.FIREFOX_REMOTE, GENERATOR.generate(), GENERATOR.generate()),
                Arguments.of(DriverType.CHROME_REMOTE_HEADLESS, GENERATOR.generate(), GENERATOR.generate()),
                Arguments.of(DriverType.FIREFOX_REMOTE_HEADLESS, GENERATOR.generate(), GENERATOR.generate())
        );
    }

    private static Stream<Arguments> provideCustomerTestDataFromYml() {
        return Objects.requireNonNull(TestDataProvider.provideCustomerData())
                .getData()
                .stream()
                .map(data -> Arguments.of(
                        data.getDriverType(),
                        data.getCustomers().iterator().next(),
                        data.getCustomers().iterator().next()
                ));
    }
}
