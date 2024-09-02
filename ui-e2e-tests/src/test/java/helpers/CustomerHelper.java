package helpers;

import org.springsandbox.domain.Customer;
import org.springsandbox.pages.CreateCustomerForm;
import org.springsandbox.pages.UpdateCustomerForm;

import java.util.Objects;

public class CustomerHelper {
    public static void createCustomer(CreateCustomerForm createCustomerForm, Customer customer) {
        createCustomerForm.enterValueToNameInput(customer.getName());
        createCustomerForm.enterValueToEmailInput(customer.getEmail());
        createCustomerForm.enterValueToAgeInput(String.valueOf(customer.getAge()));
        createCustomerForm.selectValueInGenderSelect(customer.getGender());
        createCustomerForm.clickSubmitCustomerButton();
    }

    public static void editCustomer(UpdateCustomerForm updateCustomerForm, Customer customer) {
        if (
                Objects.nonNull(customer.getName())
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
}
