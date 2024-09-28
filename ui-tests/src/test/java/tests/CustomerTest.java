package tests;

import generators.CustomerGenerator;
import helpers.CustomerHelper;
import io.qameta.allure.*;
import matchers.CustomerMatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springsandbox.domain.Customer;
import org.springsandbox.enums.DriverType;
import org.springsandbox.pages.CreateCustomerForm;
import org.springsandbox.pages.IndexPage;
import org.springsandbox.pages.UpdateCustomerForm;
import utils.TestDataProvider;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.TestStep.step;

@Epic("Customer UI tests")
@Tag("customer")
public class CustomerTest extends BaseTest {
    private static final CustomerGenerator GENERATOR = new CustomerGenerator();

    @ParameterizedTest(name = "{displayName} (driver: {0})")
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
        step("Go to index page", logger, indexPage::goTo);
        step("Click create customer button", logger, indexPage::clickCreateCustomerButton);
        step("Fill in create customer form", logger, () -> {
            var createCustomerForm = new CreateCustomerForm(driver);
            CustomerHelper.createCustomer(createCustomerForm, customer);
        });
        // Assert
        var createdCustomerCard = step("Find created customer card on index page",
                logger, () -> indexPage.getCustomerCardWithEmail(customer.getEmail()));
        step("Check that new customer card is displayed on index page",
                logger, () -> assertThat(createdCustomerCard).isNotNull());
        // TODO: add success toast isDisplayed check
        step("Check that data on that card is the same as generated", logger, () ->
                CustomerMatchers.formContainsCustomer(indexPage, createdCustomerCard, customer));
        // Cleanup
        step("Delete created customer", logger, () -> {
            indexPage.clickDeleteCustomer(createdCustomerCard);
            indexPage.confirmDeleteCustomer();
            assertThat(indexPage.getCustomerCardWithEmail(customer.getEmail())).isNull();
        });
    }

    @ParameterizedTest(name = "{displayName} (driver: {0})")
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
        step("Go to index page", logger, indexPage::goTo);
        step("Click create customer button", logger, indexPage::clickCreateCustomerButton);
        step("Fill in create customer form with initial data", logger, () -> {
            var createCustomerForm = new CreateCustomerForm(driver);
            CustomerHelper.createCustomer(createCustomerForm, initialCustomer);
        });
        var createdCustomerCard = step("Find created customer card on index page", logger,
                () -> indexPage.getCustomerCardWithEmail(initialCustomer.getEmail()));
        step("Click edit customer button", logger, () -> indexPage.clickEditCustomer(createdCustomerCard));
        step("Edit customer with updated data", logger, () -> {
            var updateCustomerForm = new UpdateCustomerForm(driver);
            CustomerHelper.editCustomer(updateCustomerForm, updatedCustomer);
        });
        // Assert
        var updatedCustomerCard = step("Find updated customer card",
                logger, () -> indexPage.getCustomerCardWithEmail(updatedCustomer.getEmail()));
        assertThat(updatedCustomerCard).isNotNull();
        // TODO: add success toast isDisplayed check
        step("Check that data on that card is as generated", logger, () ->
                CustomerMatchers.formContainsCustomer(indexPage, updatedCustomerCard, updatedCustomer));
        // Cleanup
        step("Delete customer", logger, () -> {
            indexPage.clickDeleteCustomer(updatedCustomerCard);
            indexPage.confirmDeleteCustomer();
            assertThat(indexPage.getCustomerCardWithEmail(updatedCustomer.getEmail())).isNull();
        });
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
