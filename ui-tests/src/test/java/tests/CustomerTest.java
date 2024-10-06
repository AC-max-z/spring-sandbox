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
import org.springsandbox.config.test_data.TestDataProvider;

import java.util.*;
import java.util.stream.Stream;

import static utils.TestStep.step;

@Epics({
        @Epic("Customer UI tests"),
        @Epic("UI tests"),
        @Epic("Awesome epic"),
        @Epic("E2E tests")
})
@Tag("customer")
public class CustomerTest extends BaseTest {
    private static final CustomerGenerator GENERATOR = new CustomerGenerator();
    private static final ThreadLocal<Customer> CUSTOMER_FOR_DELETION_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<IndexPage> INDEX_PAGE_THREAD_LOCAL = new ThreadLocal<>();

    @ParameterizedTest(name = "{displayName} (driver type: {0})")
    @MethodSource("provideDriverTypesAndCustomers")
    @DisplayName("Should display new customer after creating one")
    @Description("This test creates new customer, checks if it is present on page, then deletes it")
    @Tags({
            @Tag("positive"),
            @Tag("regression"),
            @Tag("acceptance")
    })
    @Features({
            @Feature("Awesome feature"),
            @Feature("Another awesome feature")
    })
    @Stories({
            @Story("Create customer"),
            @Story("Delete customer")
    })
    @Links({
            @Link(name = "Customer API", url = "https://"),
            @Link(name = "Customer UI", url = "https://")
    })
    @Issues({
            @Issue("JIRA-123"),
            @Issue("JIRA-124")
    })
    @TmsLinks({
            @TmsLink("TMS-123"),
            @TmsLink("TMS-124")
    })
    @Owner("John Doe")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldDisplayNewCustomerAfterCreate(
            DriverType driverType,
            Customer customer,
            Customer updatedCustomer
    ) throws Exception {
        // Arrange
        var logger = LOGGER_THREAD_LOCAL.get();
        var driver = step("Create driver instance", logger, () -> setupDriver(driverType));
        var indexPage = step("Init index page", logger, () -> new IndexPage(driver));
        CUSTOMER_FOR_DELETION_THREAD_LOCAL.set(customer);
        INDEX_PAGE_THREAD_LOCAL.set(indexPage);
        // Act
        step("Go to index page", logger, indexPage::goTo);
        CustomerHelper.createNewCustomer(driver, indexPage, customer, logger);
        // Assert
        // TODO: add success toast isDisplayed check
        CustomerHelper.verifyPageContainsCustomerCardSoftly(indexPage, customer, logger);
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
        var indexPage = step("Init index page", logger, () -> new IndexPage(driver));
        CUSTOMER_FOR_DELETION_THREAD_LOCAL.set(updatedCustomer);
        INDEX_PAGE_THREAD_LOCAL.set(indexPage);
        // Act
        step("Go to index page", logger, indexPage::goTo);
        CustomerHelper.createNewCustomer(driver, indexPage, initialCustomer, logger);
        // TODO: add success toast isDisplayed check
        CustomerHelper.verifyPageContainsCustomerCardSoftly(indexPage, initialCustomer, logger);
        CustomerHelper.editCustomer(driver, indexPage, initialCustomer, updatedCustomer, logger);
        // Assert
        // TODO: add success toast isDisplayed check
        CustomerHelper.verifyPageContainsCustomerCardSoftly(indexPage, updatedCustomer, logger);
    }

    @Override
    @AfterEach
    void tearDown() {
        // Cleans up created customer after each test...(even if it fails early)
        // (I know, that it makes little sense,
        // just for demo purposes of how to handle cleanups)
        if (Objects.nonNull(CUSTOMER_FOR_DELETION_THREAD_LOCAL.get())
                && Objects.nonNull(INDEX_PAGE_THREAD_LOCAL.get())) {
            try {
                CustomerHelper.deleteCustomer(
                        INDEX_PAGE_THREAD_LOCAL.get(),
                        CUSTOMER_FOR_DELETION_THREAD_LOCAL.get(),
                        LOGGER_THREAD_LOCAL.get()
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                INDEX_PAGE_THREAD_LOCAL.remove();
                CUSTOMER_FOR_DELETION_THREAD_LOCAL.remove();
            }
        }
        super.tearDown();
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
