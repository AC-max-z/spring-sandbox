import generators.CustomerGenerator;
import helpers.CustomerHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
    public void shouldDisplayNewCustomerAfterCreate(DriverType driverType, Customer customer, Customer updatedCustomer) throws MalformedURLException, URISyntaxException {
        // Arrange
        step("Create driver instance %s".formatted(driverType));
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        var indexPage = new IndexPage(driver);

        // Act
        step("Go to index page");
        indexPage.goTo();
        step("Click create customer button");
        indexPage.clickCreateCustomerButton();
        step("Fill in create customer form " + customer.toString());
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        CustomerHelper.createCustomer(createCustomerForm, customer);

        // Assert
        step("Check that new customer card is displayed on index page");
        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(customer.getEmail());
        assertThat(createdCustomerCard).isNotNull();
        // TODO: add success toast isDisplayed check
        step("Check that data on that card is the same as generated");
        assertThat(indexPage.getCustomerNameFromCard(createdCustomerCard)).isEqualTo(customer.getName());
        assertThat(indexPage.getCustomerAgeFromCard(createdCustomerCard)).isEqualTo(customer.getAge());
        assertThat(indexPage.getCustomerGenderFromCard(createdCustomerCard)).isEqualTo(customer.getGender());

        // Cleanup
        step("Delete created customer");
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
    void shouldDisplayUpdatedCustomerAfterEdit(DriverType driverType, Customer initialCustomer, Customer updatedCustomer) throws MalformedURLException, URISyntaxException {
        // Arrange
        step("Create driver instance %s".formatted(driverType));
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        var indexPage = new IndexPage(driver);

        // Act
        step("Go to index page");
        indexPage.goTo();
        step("Click create customer button");
        indexPage.clickCreateCustomerButton();
        step("Fill in create customer form with initial data " + initialCustomer.toString());
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        CustomerHelper.createCustomer(createCustomerForm, initialCustomer);

        step("Find created customer card on index page");
        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(initialCustomer.getEmail());
        indexPage.clickEditCustomer(createdCustomerCard);

        step("Click edit customer button");
        UpdateCustomerForm updateCustomerForm = new UpdateCustomerForm(driver);
        step("Edit customer with updated data " + updatedCustomer.toString());
        CustomerHelper.editCustomer(updateCustomerForm, updatedCustomer);

        // Assert
        step("Find updated customer card");
        WebElement updatedCustomerCard = indexPage.getCustomerCardWithEmail(updatedCustomer.getEmail());
        assertThat(updatedCustomerCard).isNotNull();
        // TODO: add success toast isDisplayed check
        step("Check that data on that card is as generated");
        assertThat(indexPage.getCustomerNameFromCard(updatedCustomerCard)).isEqualTo(updatedCustomer.getName());
        assertThat(indexPage.getCustomerAgeFromCard(updatedCustomerCard)).isEqualTo(updatedCustomer.getAge());
        assertThat(indexPage.getCustomerGenderFromCard(updatedCustomerCard)).isEqualTo(updatedCustomer.getGender());

        // Cleanup
        step("Delete customer");
        indexPage.clickDeleteCustomer(updatedCustomerCard);
        indexPage.confirmDeleteCustomer();
        assertThat(indexPage.getCustomerCardWithEmail(updatedCustomer.getEmail())).isNull();

    }

    private static Stream<Arguments> provideCustomersAndDriverTypes() {
        return Stream.of(
                Arguments.of(DriverType.CHROME_REMOTE, generator.generate(), generator.generate()),
                Arguments.of(DriverType.FIREFOX_REMOTE, generator.generate(), generator.generate()),
                Arguments.of(DriverType.CHROME_REMOTE, generator.generate(), generator.generate()),
                Arguments.of(DriverType.FIREFOX_REMOTE, generator.generate(), generator.generate())
        );
    }
}
