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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
public class SimpleSeleniumScriptTest {

    private ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private Faker faker = new Faker();

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
    public void createNewCustomerAndVerifyItIsDisplayed(DriverType driverType) throws MalformedURLException, URISyntaxException {
        // Arrange
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();
        var indexPage = new IndexPage(driver);
        String name = faker.name().firstName();
        String email =name + "@" + faker.internet().domainName();
        Integer age = faker.number().numberBetween(16, 99);
        List<String> availableGenders = List.of("MALE", "FEMALE", "NONE_OF_YOUR_BUSINESS");
        String pseudoRandomlyPickedGender = availableGenders.get(new Random().nextInt(availableGenders.size()));

        // Act
        indexPage.goTo();
        indexPage.clickCreateCustomerButton();
        CreateCustomerForm createCustomerForm = new CreateCustomerForm(driver);
        createCustomerForm.enterValueToNameInput(name);
        createCustomerForm.enterValueToEmailInput(email);
        createCustomerForm.enterValueToAgeInput(age.toString());
        createCustomerForm.selectValueInGenderSelect(pseudoRandomlyPickedGender);
        createCustomerForm.clickSubmitCustomerButton();

        // Assert
        WebElement createdCustomerCard = indexPage.getCustomerCardWithEmail(email);
        assertThat(createdCustomerCard).isNotNull();
        assertThat(indexPage.getCustomerNameFromCard(createdCustomerCard)).isEqualTo(name);
        assertThat(indexPage.getCustomerAgeFromCard(createdCustomerCard)).isEqualTo(age);
        assertThat(indexPage.getCustomerGenderFromCard(createdCustomerCard)).isEqualTo(pseudoRandomlyPickedGender);

        indexPage.clickDeleteCustomer(createdCustomerCard);
        indexPage.confirmDeleteCustomer();

        assertThat(indexPage.getCustomerCardWithEmail(email)).isNull();
    }
}
