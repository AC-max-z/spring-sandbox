package org.springsandbox.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Random;

public class CreateCustomerForm extends BasePage {

    public CreateCustomerForm(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//input[contains(@name, 'name')]")
    private WebElement nameInput;

    @FindBy(xpath = "//input[contains(@name, 'email')]")
    private WebElement emailInput;

    @FindBy(xpath = "//input[contains(@name, 'age')]")
    private WebElement ageInput;

    @FindBy(xpath = "//select[contains(@name, 'gender')]")
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

    public void enterName(String value) {
        waitAndEnterValueToInput(value, nameInput);
    }

    public void enterEmail(String value) {
        waitAndEnterValueToInput(value, emailInput);
    }

    public void enterAge(String value) {
        waitAndEnterValueToInput(value, ageInput);
    }

    public void selectGender(String value) {
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
