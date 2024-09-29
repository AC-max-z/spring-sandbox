package helpers;

import io.qameta.allure.Step;
import matchers.CustomerMatchers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.springsandbox.domain.Customer;
import org.springsandbox.pages.CreateCustomerForm;
import org.springsandbox.pages.IndexPage;
import org.springsandbox.pages.UpdateCustomerForm;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.TestStep.step;

public class CustomerHelper {
    private static void createCustomer(CreateCustomerForm createCustomerForm, Customer customer) {
        createCustomerForm.enterValueToNameInput(customer.getName());
        createCustomerForm.enterValueToEmailInput(customer.getEmail());
        createCustomerForm.enterValueToAgeInput(String.valueOf(customer.getAge()));
        createCustomerForm.selectValueInGenderSelect(customer.getGender());
        createCustomerForm.clickSubmitCustomerButton();
    }

    private static void editCustomer(UpdateCustomerForm updateCustomerForm, Customer customer) {
        if (Objects.nonNull(customer.getName())
                && !updateCustomerForm
                .getNameInput()
                .getAttribute("value")
                .equals(customer.getName())
        ) {
            updateCustomerForm.enterValueToNameInput(customer.getName());
        }
        if (Objects.nonNull(customer.getEmail())
                && !updateCustomerForm
                .getEmailInput()
                .getAttribute("value")
                .equals(customer.getEmail())
        ) {
            updateCustomerForm.enterValueToEmailInput(customer.getEmail());
        }
        if (!updateCustomerForm
                .getAgeInput()
                .getAttribute("value")
                .equals(String.valueOf(customer.getAge()))
        ) {
            updateCustomerForm.enterValueToAgeInput(String.valueOf(customer.getAge()));
        }
        if (Objects.nonNull(customer.getGender())) {
            updateCustomerForm.selectValueInGenderSelect(customer.getGender());
        }
        updateCustomerForm.clickSubmitCustomerButton();
    }

    @Step("Create new customer")
    public static void createNewCustomer(
            WebDriver driver,
            IndexPage indexPage,
            Customer customer,
            Logger logger
    ) {
        step("Go to index page", logger, indexPage::goTo);
        step("Click create customer button", logger, indexPage::clickCreateCustomerButton);
        step("Fill in create customer form", logger, () -> {
            var createCustomerForm = new CreateCustomerForm(driver);
            createCustomer(createCustomerForm, customer);
        });
    }

    @Step("Verify page contains customer")
    public static WebElement verifyPageContainsCustomer(
            IndexPage indexPage,
            Customer customer,
            Logger logger
    ) throws Exception {
        var customerCard = step("Find created customer card on index page",
                logger, () -> indexPage.getCustomerCardWithEmail(customer.getEmail()));
        step("Check that new customer card is displayed on index page",
                logger, () -> assertThat(customerCard).isNotNull());
        // TODO: add success toast isDisplayed check
        step("Check that data on that card is the same as generated", logger, () ->
                CustomerMatchers.formContainsCustomer(indexPage, customerCard, customer));
        return customerCard;
    }

    @Step("Edit customer")
    public static void editCustomer(
            WebDriver driver,
            IndexPage indexPage,
            Customer initialCustomer,
            Customer updatedCustomer,
            Logger logger
    ) throws Exception {
        var customerCard = step("Find created customer card on index page",
                logger, () -> indexPage.getCustomerCardWithEmail(initialCustomer.getEmail()));
        step("Click edit customer button", logger, () -> indexPage.clickEditCustomer(customerCard));
        step("Edit customer with updated data", logger, () -> {
            var updateCustomerForm = new UpdateCustomerForm(driver);
            editCustomer(updateCustomerForm, updatedCustomer);
        });
    }
}
