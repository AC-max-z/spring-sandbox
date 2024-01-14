package org.springsandbox.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.springsandbox.enums.WaitCondition;
import org.springsandbox.waits.Waits;

import java.util.List;
import java.util.Random;

public class UpdateCustomerForm {
    private final WebDriver driver;
    private final Waits waits;

    public UpdateCustomerForm(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        this.waits = new Waits(this.driver);
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
        waits.waitForElement(WaitCondition.VISIBLE, nameInput);
        return nameInput;
    }

    public WebElement getEmailInput() {
        waits.waitForElement(WaitCondition.VISIBLE, emailInput);
        return emailInput;
    }

    public WebElement getAgeInput() {
        waits.waitForElement(WaitCondition.VISIBLE, ageInput);
        return ageInput;
    }

    public Select getGenderSelect() {
        waits.waitForElement(WaitCondition.VISIBLE, genderSelect);
        return new Select(genderSelect);
    }

    public List<WebElement> getGenderSelectOptions() {
        waits.waitForElements(WaitCondition.VISIBLE, genderSelectOptions);
        return genderSelectOptions;
    }

    public void enterValueToNameInput(String value) {
        waits.waitForElement(WaitCondition.CLICKABLE, nameInput);
        nameInput.click();
        nameInput.clear();
        nameInput.sendKeys(Keys.CONTROL + "A");
        nameInput.sendKeys(Keys.BACK_SPACE);
        nameInput.sendKeys(value);
    }

    public void enterValueToEmailInput(String value) {
        waits.waitForElement(WaitCondition.CLICKABLE, emailInput);
        emailInput.click();
        emailInput.clear();
        emailInput.sendKeys(Keys.CONTROL + "A");
        emailInput.sendKeys(Keys.BACK_SPACE);
        emailInput.sendKeys(value);
    }

    public void enterValueToAgeInput(String value) {
        waits.waitForElement(WaitCondition.CLICKABLE, ageInput);
        ageInput.click();
        ageInput.clear();
        ageInput.sendKeys(Keys.CONTROL + "A");
        ageInput.sendKeys(Keys.BACK_SPACE);
        ageInput.sendKeys(value);
    }

    public void selectValueInGenderSelect(String value) {
        waits.waitForElement(WaitCondition.CLICKABLE, genderSelect);
        getGenderSelect().selectByValue(value);
    }

    public void selectRandomValueInGenderSelect() {
        waits.waitForElement(WaitCondition.CLICKABLE, genderSelect);
        Integer optionsSize = getGenderSelectOptions().size();
        getGenderSelect().selectByIndex(new Random().nextInt(optionsSize));
    }

    public void clickSubmitCustomerButton() {
        waits.waitForElement(WaitCondition.CLICKABLE, submitCustomerButton);
        submitCustomerButton.click();
    }
}
