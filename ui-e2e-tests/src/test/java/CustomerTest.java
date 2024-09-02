import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springsandbox.enums.DriverType;
import org.springsandbox.factories.WebDriverFactory;
import org.springsandbox.pages.CreateCustomerForm;
import org.springsandbox.pages.IndexPage;
import org.springsandbox.pages.UpdateCustomerForm;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;

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
    private final Faker faker = new Faker();

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
    @EnumSource(DriverType.class)
    @DisplayName("Should display new customer after creating one")
    @Description("This test creates new customer, checks if it is present on page, then deletes it")
    @Tags({
            @Tag("positive"),
            @Tag("regression"),
            @Tag("acceptance")
    })
    @Severity(SeverityLevel.BLOCKER)
    public void shouldDisplayNewCustomerAfterCreate(DriverType driverType) throws MalformedURLException, URISyntaxException {
        // Arrange
        step("Create driver instance %s".formatted(driverType));
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        var indexPage = new IndexPage(driver);

        step("Generate new customer data");
        String name = faker.name().firstName();
        String email = name + "@" + faker.internet().domainName();
        Integer age = faker.number().numberBetween(16, 99);
        List<String> availableGenders = List.of("MALE", "FEMALE", "NONE_OF_YOUR_BUSINESS");
        String pseudoRandomlyPickedGender = availableGenders.get(new Random().nextInt(availableGenders.size()));

        // Act
        step("Go to index page");
        indexPage.goTo();
        step("Click create customer button");
        indexPage.clickCreateCustomerButton();
        step("Fill in create customer form");
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        helpers.Customer.createCustomer(createCustomerForm, name, email, age, pseudoRandomlyPickedGender);

        // Assert
        step("Check that new customer card is displayed on index page");
        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(email);
        assertThat(createdCustomerCard).isNotNull();
        // TODO: add success toast isDisplayed check
        step("Check that data on that card is the same as generated");
        assertThat(indexPage.getCustomerNameFromCard(createdCustomerCard)).isEqualTo(name);
        assertThat(indexPage.getCustomerAgeFromCard(createdCustomerCard)).isEqualTo(age);
        assertThat(indexPage.getCustomerGenderFromCard(createdCustomerCard)).isEqualTo(pseudoRandomlyPickedGender);

        // Cleanup
        step("Delete created customer");
        indexPage.clickDeleteCustomer(createdCustomerCard);
        indexPage.confirmDeleteCustomer();
        assertThat(indexPage.getCustomerCardWithEmail(email)).isNull();
    }

    @ParameterizedTest
    @EnumSource(DriverType.class)
    @DisplayName("Should display updated customer data after editing one")
    @Description("This test creates new customer, then edits it, checks if customer was updated and then deletes it")
    @Tags({
            @Tag("positive"),
            @Tag("regression"),
            @Tag("acceptance")
    })
    @Severity(SeverityLevel.BLOCKER)
    void shouldDisplayUpdatedCustomerAfterEdit(DriverType driverType) throws MalformedURLException, URISyntaxException {
        // Arrange
        step("Create driver instance %s".formatted(driverType));
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        var indexPage = new IndexPage(driver);

        step("Generate initial customer data");
        String initialName = faker.name().firstName();
        String initialEmail = initialName + "@" + faker.internet().domainName();
        Integer initialAge = faker.number().numberBetween(16, 99);
        List<String> availableGenders = List.of("MALE", "FEMALE", "NONE_OF_YOUR_BUSINESS");
        String initialPseudoRandomlyPickedGender = availableGenders.get(new Random().nextInt(availableGenders.size()));

        step("Generate updated customer data");
        String updatedName = faker.name().firstName();
        String updatedEmail = initialName + "@" + faker.internet().domainName();
        Integer updatedAge = faker.number().numberBetween(16, 99);
        String updatedPseudoRandomlyPickedGender = availableGenders.get(new Random().nextInt(availableGenders.size()));

        // Act
        step("Go to index page");
        indexPage.goTo();
        step("Click create customer button");
        indexPage.clickCreateCustomerButton();
        step("Fill in create customer form with initial data");
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        helpers.Customer.createCustomer(createCustomerForm, initialName, initialEmail, initialAge, initialPseudoRandomlyPickedGender);

        step("Find created customer card on index page");
        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(initialEmail);
        indexPage.clickEditCustomer(createdCustomerCard);

        step("Click edit customer button");
        UpdateCustomerForm updateCustomerForm = new UpdateCustomerForm(driver);
        helpers.Customer.editCustomer(updateCustomerForm, updatedName, updatedEmail, updatedAge, updatedPseudoRandomlyPickedGender);

        // Assert
        step("Find updated customer card");
        WebElement updatedCustomerCard = indexPage.getCustomerCardWithEmail(updatedEmail);
        assertThat(updatedCustomerCard).isNotNull();
        // TODO: add success toast isDisplayed check
        step("Check that data on that card is as generated");
        assertThat(indexPage.getCustomerNameFromCard(updatedCustomerCard)).isEqualTo(updatedName);
        assertThat(indexPage.getCustomerAgeFromCard(updatedCustomerCard)).isEqualTo(updatedAge);
        assertThat(indexPage.getCustomerGenderFromCard(updatedCustomerCard)).isEqualTo(updatedPseudoRandomlyPickedGender);

        // Cleanup
        step("Delete customer");
        indexPage.clickDeleteCustomer(updatedCustomerCard);
        indexPage.confirmDeleteCustomer();
        assertThat(indexPage.getCustomerCardWithEmail(updatedEmail)).isNull();

    }
}
