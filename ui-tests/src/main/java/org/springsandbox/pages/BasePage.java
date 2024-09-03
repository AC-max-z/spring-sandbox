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

/**
 * Abstract page class for other page object classes to inherit from
 * that contains common actions
 * (everything is pretty self-explanatory, but verbosely commented just in case)
 */
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

    /**
     * Decorator method that waits until the element is visible
     * and returns provided element back
     * Re-Initializes the page if element is stale and retries
     *
     * @param element - WebElement
     * @return - provided Element
     */
    protected WebElement getElement(WebElement element) {
        try {
            waits.waitForElement(WaitCondition.VISIBLE, element);
        } catch (StaleElementReferenceException e) {
            PageFactory.initElements(driver, this);
            waits.waitForElement(WaitCondition.VISIBLE, element);
        }
        return element;
    }

    /**
     * Decorator method that waits until the element is clickable
     * and returns provided element back
     * Re-Initializes the page if element is stale and retries
     *
     * @param element - WebElement
     * @return - provided Element
     */
    protected WebElement getClickableElement(WebElement element) {
        try {
            waits.waitForElement(WaitCondition.CLICKABLE, element);
        } catch (StaleElementReferenceException e) {
            PageFactory.initElements(driver, this);
            waits.waitForElement(WaitCondition.CLICKABLE, element);
        }
        return element;
    }

    /**
     * Decorator method that waits until all elements from the list are visible
     * and returns provided elements list back
     * Re-Initializes the page if StaleElementReferenceException is thrown and retries
     *
     * @param elements - List of WebElements
     * @return - provided List of WebElements
     */
    protected List<WebElement> getElements(List<WebElement> elements) {
        try {
            waits.waitForElements(WaitCondition.VISIBLE, elements);
        } catch (StaleElementReferenceException e) {
            PageFactory.initElements(driver, this);
            waits.waitForElements(WaitCondition.VISIBLE, elements);
        }
        return elements;
    }

    /**
     * Decorator method that waits until element is clickable and clicks it
     *
     * @param element - WebElement to click
     */
    protected void clickElement(WebElement element) {
        getClickableElement(element);
        element.click();
    }

    /**
     * Decorator method that waits until provided input element is clickable
     * and enters provided value in it
     *
     * @param value - value to enter
     * @param input - input WebElement
     */
    protected void enterValueToInput(String value, WebElement input) {
        getClickableElement(input);
        input.click();
        input.clear();
        input.sendKeys(Keys.CONTROL + "A");
        input.sendKeys(Keys.BACK_SPACE);
        input.sendKeys(value);
    }

    /**
     * Decorator method that waits until provided select WebElement is clickable
     * and selects it
     *
     * @param value  - value to select
     * @param select - select type WebElement
     */
    protected void selectValueInSelect(String value, WebElement select) {
        getClickableElement(select);
        new Select(select).selectByValue(value);
    }
}
