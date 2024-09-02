package org.springsandbox.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.springsandbox.enums.WaitCondition;
import org.springsandbox.util.AppProperties;
import org.springsandbox.waits.Waits;

import java.util.List;
import java.util.Map;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final Map<String, String> envVars = AppProperties.getProperties();
    protected final String baseUrl = envVars.get("APP_URL");
    protected final Waits waits;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        this.waits = new Waits(this.driver);
    }

    public String getPageUrl() {
        return baseUrl;
    }

    public void goTo() {
        driver.get(baseUrl);
    }

    protected WebElement getElement(WebElement element) {
        try {
            waits.waitForElement(WaitCondition.VISIBLE, element);
        } catch (StaleElementReferenceException e) {
            PageFactory.initElements(driver, this);
            waits.waitForElement(WaitCondition.VISIBLE, element);
        }
        return element;
    }

    protected List<WebElement> getElements(List<WebElement> elements) {
        try {
            waits.waitForElements(WaitCondition.CLICKABLE, elements);
        } catch (StaleElementReferenceException e) {
            PageFactory.initElements(driver, this);
            waits.waitForElements(WaitCondition.CLICKABLE, elements);
        }
        return elements;
    }

    protected void clickElement(WebElement element) {
        getElement(element);
        element.click();
    }

    protected void enterValueToInput(String value, WebElement input) {
        getElement(input);
        input.click();
        input.clear();
        input.sendKeys(Keys.CONTROL + "A");
        input.sendKeys(Keys.BACK_SPACE);
        input.sendKeys(value);
    }

    protected void selectValueInSelect(String value, WebElement select) {
        getElement(select);
        new Select(select).selectByValue(value);
    }
}
