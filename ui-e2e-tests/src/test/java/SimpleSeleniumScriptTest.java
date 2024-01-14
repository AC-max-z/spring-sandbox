import com.github.javafaker.Faker;
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
import helpers.Customer;
import org.springsandbox.pages.UpdateCustomerForm;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
public class SimpleSeleniumScriptTest {

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
    public void shouldDisplayNewCustomerAfterCreate(DriverType driverType) throws MalformedURLException, URISyntaxException {
        // Arrange
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        var indexPage = new IndexPage(driver);
        String name = faker.name().firstName();
        String email = name + "@" + faker.internet().domainName();
        Integer age = faker.number().numberBetween(16, 99);
        List<String> availableGenders = List.of("MALE", "FEMALE", "NONE_OF_YOUR_BUSINESS");
        String pseudoRandomlyPickedGender = availableGenders.get(new Random().nextInt(availableGenders.size()));

        // Act
        indexPage.goTo();
        indexPage.clickCreateCustomerButton();
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        Customer.createCustomer(createCustomerForm, name, email, age, pseudoRandomlyPickedGender);

        // Assert
        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(email);
        assertThat(createdCustomerCard).isNotNull();
        // TODO: add success toast isDisplayed check
        assertThat(indexPage.getCustomerNameFromCard(createdCustomerCard)).isEqualTo(name);
        assertThat(indexPage.getCustomerAgeFromCard(createdCustomerCard)).isEqualTo(age);
        assertThat(indexPage.getCustomerGenderFromCard(createdCustomerCard)).isEqualTo(pseudoRandomlyPickedGender);

        // Cleanup
        indexPage.clickDeleteCustomer(createdCustomerCard);
        indexPage.confirmDeleteCustomer();
        assertThat(indexPage.getCustomerCardWithEmail(email)).isNull();
    }

    @ParameterizedTest
    @EnumSource(DriverType.class)
    void shouldDisplayUpdatedCustomerAfterEdit(DriverType driverType) throws MalformedURLException, URISyntaxException {
        // Arrange
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        var indexPage = new IndexPage(driver);

        String initialName = faker.name().firstName();
        String initialEmail = initialName + "@" + faker.internet().domainName();
        Integer initialAge = faker.number().numberBetween(16, 99);
        List<String> availableGenders = List.of("MALE", "FEMALE", "NONE_OF_YOUR_BUSINESS");
        String initialPseudoRandomlyPickedGender = availableGenders.get(new Random().nextInt(availableGenders.size()));

        String updatedName = faker.name().firstName();
        String updatedEmail = initialName + "@" + faker.internet().domainName();
        Integer updatedAge = faker.number().numberBetween(16, 99);
        String updatedPseudoRandomlyPickedGender = availableGenders.get(new Random().nextInt(availableGenders.size()));

        // Act
        indexPage.goTo();
        indexPage.clickCreateCustomerButton();
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        Customer.createCustomer(createCustomerForm, initialName, initialEmail, initialAge, initialPseudoRandomlyPickedGender);

        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(initialEmail);
        indexPage.clickEditCustomer(createdCustomerCard);

        UpdateCustomerForm updateCustomerForm = new UpdateCustomerForm(driver);
        Customer.editCustomer(updateCustomerForm, updatedName, updatedEmail, updatedAge, updatedPseudoRandomlyPickedGender);

        // Assert
        WebElement updatedCustomerCard = indexPage.getCustomerCardWithEmail(updatedEmail);
        assertThat(updatedCustomerCard).isNotNull();
        // TODO: add success toast isDisplayed check
        assertThat(indexPage.getCustomerNameFromCard(updatedCustomerCard)).isEqualTo(updatedName);
        assertThat(indexPage.getCustomerAgeFromCard(updatedCustomerCard)).isEqualTo(updatedAge);
        assertThat(indexPage.getCustomerGenderFromCard(updatedCustomerCard)).isEqualTo(updatedPseudoRandomlyPickedGender);

        // Cleanup
        indexPage.clickDeleteCustomer(updatedCustomerCard);
        indexPage.confirmDeleteCustomer();
        assertThat(indexPage.getCustomerCardWithEmail(updatedEmail)).isNull();

    }
}
