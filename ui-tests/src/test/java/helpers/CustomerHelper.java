package helpers;

import io.qameta.allure.Step;
import matchers.CustomerMatchers;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.springsandbox.domain.Customer;
import org.springsandbox.pages.CreateCustomerForm;
import org.springsandbox.pages.IndexPage;
import org.springsandbox.pages.UpdateCustomerForm;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static utils.TestStep.step;

public class CustomerHelper {
    private static void createCustomer(CreateCustomerForm createCustomerForm, Customer customer) {
        createCustomerForm.enterName(customer.getName());
        createCustomerForm.enterEmail(customer.getEmail());
        createCustomerForm.enterAge(String.valueOf(customer.getAge()));
        createCustomerForm.selectGender(customer.getGender());
        createCustomerForm.submit();
    }

    private static void editCustomer(UpdateCustomerForm updateCustomerForm, Customer customer) {
        if (Objects.nonNull(customer.getName())
                && !updateCustomerForm
                .getNameInput()
                .getAttribute("value")
                .equals(customer.getName())
        ) {
            updateCustomerForm.editName(customer.getName());
        }
        if (Objects.nonNull(customer.getEmail())
                && !updateCustomerForm
                .getEmailInput()
                .getAttribute("value")
                .equals(customer.getEmail())
        ) {
            updateCustomerForm.editEmail(customer.getEmail());
        }
        if (!updateCustomerForm
                .getAgeInput()
                .getAttribute("value")
                .equals(String.valueOf(customer.getAge()))
        ) {
            updateCustomerForm.editAge(String.valueOf(customer.getAge()));
        }
        if (Objects.nonNull(customer.getGender())) {
            updateCustomerForm.editGender(customer.getGender());
        }
        updateCustomerForm.submit();
    }

    @Step("Create new customer")
    public static void createNewCustomer(
            WebDriver driver,
            IndexPage indexPage,
            Customer customer,
            Logger logger
    ) {
        step("Click create customer button", logger, indexPage::createCustomer);
        step("Fill in create customer form", logger, () -> {
            var createCustomerForm = new CreateCustomerForm(driver);
            createCustomer(createCustomerForm, customer);
        });
    }

    @Step("Verify page contains customer card")
    public static void verifyPageContainsCustomerCard(
            IndexPage indexPage,
            Customer customer,
            Logger logger
    ) throws Exception {
        var customerCard = step("Find customer card on index page",
                logger, () -> indexPage.findCustomerCardWithEmail(customer.getEmail()));
        step("Check that customer card is displayed on index page",
                logger, () -> assertThat(customerCard).isNotNull());
        step("Check that data on that card is the same as expected", logger, () ->
                CustomerMatchers.verifyCustomerCardContainsCustomerData(indexPage, customerCard, customer));
    }

    @Step("Verify page contains customer card softly")
    public static void verifyPageContainsCustomerCardSoftly(
            IndexPage indexPage,
            Customer customer,
            Logger logger
    ) throws Exception {
        var customerCard = step("Find customer card on index page",
                logger, () -> indexPage.findCustomerCardWithEmail(customer.getEmail()));
        step("Check that customer card is displayed on index page",
                logger, () -> assertSoftly(softly ->
                        softly.assertThat(customerCard).isNotNull())
        );
        step("Check that data on that card is the same as expected", logger, () ->
                CustomerMatchers.verifyCustomerCardContainsCustomerDataSoftly(
                        indexPage, customerCard, customer));
    }

    @Step("Edit customer")
    public static void editCustomer(
            WebDriver driver,
            IndexPage indexPage,
            Customer initialCustomer,
            Customer updatedCustomer,
            Logger logger
    ) throws Exception {
        var customerCard = step("Find existing customer card on index page",
                logger, () -> indexPage.findCustomerCardWithEmail(initialCustomer.getEmail()));
        step("Click edit customer button", logger, () -> indexPage.editCustomer(customerCard));
        step("Edit customer with new data", logger, () -> {
            var updateCustomerForm = new UpdateCustomerForm(driver);
            editCustomer(updateCustomerForm, updatedCustomer);
        });
    }

    @Step("Delete customer")
    public static void deleteCustomer(
            IndexPage indexPage,
            Customer customer,
            Logger logger
    ) throws Exception {
        logger.info("Deleting customer with email: {}", customer.getEmail());
        var customerCard = step("Find customer card on index page", logger,
                () -> indexPage.findCustomerCardWithEmail(customer.getEmail()));
        step("Click delete button", logger, () -> indexPage.deleteCustomer(customerCard));
        step("Confirm customer delete action", logger, indexPage::confirmDeleteCustomer);
        step("Make sure there is no customer card left on index page", logger,
                () -> assertThat(indexPage.findCustomerCardWithEmail(customer.getEmail())).isNull());
    }
}
