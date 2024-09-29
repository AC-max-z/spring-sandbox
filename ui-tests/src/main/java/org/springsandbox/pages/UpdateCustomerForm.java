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
        return getVisibleElement(nameInput);
    }

    public WebElement getEmailInput() {
        return getVisibleElement(emailInput);
    }

    public WebElement getAgeInput() {
        return getVisibleElement(ageInput);
    }

    public Select getGenderSelect() {
        return new Select(getVisibleElement(genderSelect));
    }

    public List<WebElement> getGenderSelectOptions() {
        return getVisibleElements(genderSelectOptions);
    }

    public void editName(String value) {
        waitAndEnterValueToInput(value, nameInput);
    }

    public void editEmail(String value) {
        waitAndEnterValueToInput(value, emailInput);
    }

    public void editAge(String value) {
        waitAndEnterValueToInput(value, ageInput);
    }

    public void editGender(String value) {
        waitAndSelectValueInSelect(value, genderSelect);
    }

    public void selectRandomGender() {
        int optionsSize = getGenderSelectOptions().size();
        getGenderSelect().selectByIndex(new Random().nextInt(optionsSize));
    }

    public void submit() {
        waitAndClickElement(submitCustomerButton);
    }
}
