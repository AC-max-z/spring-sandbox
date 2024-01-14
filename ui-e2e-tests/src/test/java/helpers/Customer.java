package helpers;

import org.springsandbox.pages.CreateCustomerForm;
import org.springsandbox.pages.UpdateCustomerForm;

import java.util.Objects;

public class Customer {
    public static void createCustomer(CreateCustomerForm createCustomerForm, String name, String email, Integer age, String gender) {
        createCustomerForm.enterValueToNameInput(name);
        createCustomerForm.enterValueToEmailInput(email);
        createCustomerForm.enterValueToAgeInput(age.toString());
        createCustomerForm.selectValueInGenderSelect(gender);
        createCustomerForm.clickSubmitCustomerButton();
    }

    public static void editCustomer(UpdateCustomerForm updateCustomerForm, String name, String email, Integer age, String gender) {
        if (Objects.nonNull(name) && !updateCustomerForm.getNameInput().getAttribute("value").equals(name)) {
            updateCustomerForm.enterValueToNameInput(name);
        }
        if (Objects.nonNull(email) && !updateCustomerForm.getEmailInput().getAttribute("value").equals(email)) {
            updateCustomerForm.enterValueToEmailInput(email);
        }
        if (Objects.nonNull(age) && !updateCustomerForm.getAgeInput().getAttribute("value").equals(age.toString())) {
            updateCustomerForm.enterValueToAgeInput(age.toString());
        }
        if (Objects.nonNull(gender)) {
            updateCustomerForm.selectValueInGenderSelect(gender);
        }
        updateCustomerForm.clickSubmitCustomerButton();
    }
}
