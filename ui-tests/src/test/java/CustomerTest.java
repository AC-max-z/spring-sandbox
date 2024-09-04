import generators.CustomerGenerator;
import helpers.CustomerHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import matchers.CustomerMatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.SLF4JServiceProvider;
import org.springsandbox.domain.Customer;
import org.springsandbox.enums.DriverType;
import org.springsandbox.factories.WebDriverFactory;
import org.springsandbox.pages.CreateCustomerForm;
import org.springsandbox.pages.IndexPage;
import org.springsandbox.pages.UpdateCustomerForm;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
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

    @AfterEach
    void tearDown() {
        if (Objects.nonNull(driverThreadLocal.get())) {
            driverThreadLocal.get().quit();
        }
    }

    @ParameterizedTest
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
        loggerThreadLocal.set(LoggerFactory.getLogger(testInfo.getTestClass().get().getName()
                + ":" + driverType));
        var logger = loggerThreadLocal.get();

        var step = "Create driver instance %s".formatted(driverType);
        step(step);
        logger.info(step);
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        var indexPage = new IndexPage(driver);

        // Act
        var step1 = "Go to index page";
        step(step1);
        logger.info(step1);
        indexPage.goTo();

        var step2 = "Click create customer button";
        step(step2);
        logger.info(step2);
        indexPage.clickCreateCustomerButton();

        var step3 = "Fill in create customer form " + customer.toString();
        step(step3);
        logger.info(step3);
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        CustomerHelper.createCustomer(createCustomerForm, customer);

        // Assert
        var step4 = "Check that new customer card is displayed on index page";
        step(step4);
        logger.info(step4);
        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(customer.getEmail());
        assertThat(createdCustomerCard).isNotNull();

        // TODO: add success toast isDisplayed check
        var step5 = "Check that data on that card is the same as generated";
        step(step5);
        logger.info(step5);
        CustomerMatchers.formContainsCustomer(indexPage, createdCustomerCard, customer);

        // Cleanup
        var step6 = "Delete created customer";
        step(step6);
        logger.info(step6);
        indexPage.clickDeleteCustomer(createdCustomerCard);
        indexPage.confirmDeleteCustomer();
        assertThat(indexPage.getCustomerCardWithEmail(customer.getEmail())).isNull();
    }

    @ParameterizedTest
    @MethodSource("provideCustomersAndDriverTypes")
    @DisplayName("Should display updated customer data after editing one")
    @Description("This test creates new customer, then edits it, checks if customer was updated and then deletes it")
    @Tags({
            @Tag("positive"),
            @Tag("regression"),
            @Tag("acceptance")
    })
    @Severity(SeverityLevel.BLOCKER)
    void shouldDisplayUpdatedCustomerAfterEdit(
            DriverType driverType, Customer initialCustomer, Customer updatedCustomer, TestInfo testInfo
    ) throws MalformedURLException, URISyntaxException {
        // Arrange
        loggerThreadLocal.set(LoggerFactory.getLogger(testInfo.getTestClass().get().getName()
                + ":" + driverType));
        var logger = loggerThreadLocal.get();

        var step0 = "Create driver instance %s".formatted(driverType);
        step(step0);
        logger.info(step0);
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        var indexPage = new IndexPage(driver);

        // Act
        var step1 = "Go to index page";
        step(step1);
        logger.info(step1);
        indexPage.goTo();

        var step2 = "Click create customer button";
        step(step2);
        logger.info(step2);
        indexPage.clickCreateCustomerButton();

        var step3 = "Fill in create customer form with initial data " + initialCustomer.toString();
        step(step3);
        logger.info(step3);
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        CustomerHelper.createCustomer(createCustomerForm, initialCustomer);

        var step4 = "Find created customer card on index page";
        step(step4);
        logger.info(step4);
        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(initialCustomer.getEmail());
        indexPage.clickEditCustomer(createdCustomerCard);

        var step5 = "Click edit customer button";
        step(step5);
        logger.info(step5);
        UpdateCustomerForm updateCustomerForm = new UpdateCustomerForm(driver);

        var step6 = "Edit customer with updated data " + updatedCustomer.toString();
        step(step6);
        logger.info(step6);
        CustomerHelper.editCustomer(updateCustomerForm, updatedCustomer);

        // Assert
        var step7 = "Find updated customer card";
        step(step7);
        logger.info(step7);
        WebElement updatedCustomerCard = indexPage.getCustomerCardWithEmail(updatedCustomer.getEmail());
        assertThat(updatedCustomerCard).isNotNull();

        // TODO: add success toast isDisplayed check
        var step8 = "Check that data on that card is as generated";
        step(step8);
        logger.info(step8);
        CustomerMatchers.formContainsCustomer(indexPage, updatedCustomerCard, updatedCustomer);

        // Cleanup
        var step9 = "Delete customer";
        step(step9);
        logger.info(step9);
        indexPage.clickDeleteCustomer(updatedCustomerCard);
        indexPage.confirmDeleteCustomer();
        assertThat(indexPage.getCustomerCardWithEmail(updatedCustomer.getEmail())).isNull();

    }

    private static Stream<Arguments> provideCustomersAndDriverTypes() {
        return Stream.of(
                Arguments.of(DriverType.CHROME_REMOTE, generator.generate(), generator.generate()),
                Arguments.of(DriverType.FIREFOX_REMOTE, generator.generate(), generator.generate()),
                Arguments.of(DriverType.CHROME_REMOTE_HEADLESS, generator.generate(), generator.generate()),
                Arguments.of(DriverType.FIREFOX_REMOTE_HEADLESS, generator.generate(), generator.generate())
        );
    }
}
