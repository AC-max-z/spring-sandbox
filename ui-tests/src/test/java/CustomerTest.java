import generators.CustomerGenerator;
import helpers.CustomerHelper;
import io.qameta.allure.*;
import matchers.CustomerMatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springsandbox.domain.Customer;
import org.springsandbox.enums.DriverType;
import org.springsandbox.factories.WebDriverFactory;
import org.springsandbox.pages.CreateCustomerForm;
import org.springsandbox.pages.IndexPage;
import org.springsandbox.pages.UpdateCustomerForm;
import util.DriverLogger;
import util.ScreenshotExtension;
import util.TestDataProvider;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static util.Step.step;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith({ScreenshotExtension.class})
@Epic("Customer UI tests")
@Tags({
        @Tag("UI"),
        @Tag("E2E"),
        @Tag("customer")
})
public class CustomerTest {

    private final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final CustomerGenerator generator = new CustomerGenerator();
    private final ThreadLocal<Logger> loggerThreadLocal = new ThreadLocal<>();

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        loggerThreadLocal.set(LoggerFactory.getLogger(
                testInfo.getTestClass().get().getName()));
    }

    @AfterEach
    void tearDown() {
        if (Objects.nonNull(driverThreadLocal.get())) {
            Allure.step("Close driver");
            driverThreadLocal.get().quit();
        }
    }

    @ParameterizedTest(name = "{displayName} (driver: {0})")
    @MethodSource("provideCustomersAndDriverTypes")
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
            Customer updatedCustomer,
            TestInfo testInfo
    ) throws MalformedURLException, URISyntaxException {
        // Arrange
        var logger = loggerThreadLocal.get();

        step("Create driver instance %s".formatted(driverType), logger);
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        ScreenshotExtension.setDriver(driver);

        var indexPage = new IndexPage(driver);

        // Act
        step("Go to index page", logger);
        indexPage.goTo();

        step("Click create customer button", logger);
        indexPage.clickCreateCustomerButton();

        step("Fill in create customer form " + customer.toString(), logger);
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        CustomerHelper.createCustomer(createCustomerForm, customer);

        // Assert
        step("Check that new customer card is displayed on index page", logger);
        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(customer.getEmail());
        assertThat(createdCustomerCard).isNotNull();

        // TODO: add success toast isDisplayed check
        step("Check that data on that card is the same as generated", logger);
        CustomerMatchers.formContainsCustomer(indexPage, createdCustomerCard, customer);

        // Cleanup
        step("Delete created customer", logger);
        indexPage.clickDeleteCustomer(createdCustomerCard);
        indexPage.confirmDeleteCustomer();
        assertThat(indexPage.getCustomerCardWithEmail(customer.getEmail())).isNull();
        // collect driver logs?
        DriverLogger.log(logger, driver.manage().logs());
    }

    @ParameterizedTest(name = "{displayName} (driver: {0})")
    @MethodSource("provideCustomerDataFromYml")
    @DisplayName("Should display updated customer data after editing one")
    @Description("This test creates new customer, then edits it, checks if customer was updated and then deletes it")
    @Tags({
            @Tag("positive"),
            @Tag("regression"),
            @Tag("acceptance")
    })
    @Severity(SeverityLevel.BLOCKER)
    void shouldDisplayUpdatedCustomerAfterEdit(
            DriverType driverType,
            Customer initialCustomer,
            Customer updatedCustomer,
            TestInfo testInfo
    ) throws MalformedURLException, URISyntaxException {
        // Arrange
        var logger = loggerThreadLocal.get();

        step("Create driver instance %s".formatted(driverType), logger);
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        ScreenshotExtension.setDriver(driver);

        var indexPage = new IndexPage(driver);

        // Act
        step("Go to index page", logger);
        indexPage.goTo();

        step("Click create customer button", logger);
        indexPage.clickCreateCustomerButton();

        step("Fill in create customer form with initial data " +
                initialCustomer.toString(), logger);
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        CustomerHelper.createCustomer(createCustomerForm, initialCustomer);

        step("Find created customer card on index page", logger);
        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(
                initialCustomer.getEmail());
        indexPage.clickEditCustomer(createdCustomerCard);

        step("Click edit customer button", logger);
        UpdateCustomerForm updateCustomerForm = new UpdateCustomerForm(driver);

        step("Edit customer with updated data " + updatedCustomer.toString(), logger);
        CustomerHelper.editCustomer(updateCustomerForm, updatedCustomer);

        // Assert
        step("Find updated customer card", logger);
        WebElement updatedCustomerCard = indexPage.getCustomerCardWithEmail(
                updatedCustomer.getEmail());
        assertThat(updatedCustomerCard).isNotNull();

        // TODO: add success toast isDisplayed check
        step("Check that data on that card is as generated", logger);
        CustomerMatchers.formContainsCustomer(indexPage, updatedCustomerCard, updatedCustomer);

        // Cleanup
        step("Delete customer", logger);
        indexPage.clickDeleteCustomer(updatedCustomerCard);
        indexPage.confirmDeleteCustomer();
        assertThat(indexPage.getCustomerCardWithEmail(updatedCustomer.getEmail())).isNull();
        // collect driver logs?
        DriverLogger.log(logger, driver.manage().logs());

    }

    private static Stream<Arguments> provideCustomersAndDriverTypes() {
        return Stream.of(
                Arguments.of(DriverType.CHROME_REMOTE, generator.generate(), generator.generate()),
                Arguments.of(DriverType.FIREFOX_REMOTE, generator.generate(), generator.generate()),
                Arguments.of(DriverType.CHROME_REMOTE_HEADLESS, generator.generate(), generator.generate()),
                Arguments.of(DriverType.FIREFOX_REMOTE_HEADLESS, generator.generate(), generator.generate())
        );
    }

    private static Stream<Arguments> provideCustomerDataFromYml() {
        return Objects.requireNonNull(TestDataProvider.provideCustomerData())
                .getData()
                .stream()
                .map(data -> Arguments.of(data.getDriverType(),
                        data.getCustomers().iterator().next(),
                        data.getCustomers().iterator().next()
                ));
    }
}
