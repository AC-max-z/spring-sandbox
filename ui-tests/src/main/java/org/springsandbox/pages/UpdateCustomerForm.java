package org.springsandbox.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Random;

public class UpdateCustomerForm extends BasePage {

    public UpdateCustomerForm(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//input[@name='name']")
    private WebElement nameInput;

    @FindBy(xpath = "//input[@name='email']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@name='age']")
    private WebElement ageInput;

    @FindBy(xpath = "//select[@name='gender']")
    private WebElement genderSelect;

    @FindAll(@FindBy(xpath = "//select[contains(@name, 'gender')]//option[not(@value='')]"))
    private List<WebElement> genderSelectOptions;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitCustomerButton;

    public WebElement getNameInput() {
        return getElement(nameInput);
    }

    public WebElement getEmailInput() {
        return getElement(emailInput);
    }

    public WebElement getAgeInput() {
        return getElement(ageInput);
    }

    public Select getGenderSelect() {
        return new Select(getElement(genderSelect));
    }

    public List<WebElement> getGenderSelectOptions() {
        return getElements(genderSelectOptions);
    }

    public void enterValueToNameInput(String value) {
        enterValueToInput(value, nameInput);
    }

    public void enterValueToEmailInput(String value) {
        enterValueToInput(value, emailInput);
    }

    public void enterValueToAgeInput(String value) {
        enterValueToInput(value, ageInput);
    }

    public void selectValueInGenderSelect(String value) {
        selectValueInSelect(value, genderSelect);
    }

    public void selectRandomValueInGenderSelect() {
        int optionsSize = getGenderSelectOptions().size();
        getGenderSelect().selectByIndex(new Random().nextInt(optionsSize));
    }

    public void clickSubmitCustomerButton() {
        clickElement(submitCustomerButton);
    }
}
