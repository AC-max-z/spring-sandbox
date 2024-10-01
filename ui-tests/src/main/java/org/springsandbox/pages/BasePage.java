package org.springsandbox.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.springsandbox.config.EnvConfig;
import org.springsandbox.enums.WaitCondition;
import org.springsandbox.config.ConfigsProvider;
import org.springsandbox.utils.DriverWaitConfiguration;

import java.util.List;

/**
 * Abstract page class for other page object classes to inherit from
 * that contains common actions
 * (everything is pretty self-explanatory, but verbosely commented just in case)
 */
public abstract class BasePage {
    protected final WebDriver DRIVER;
    private final EnvConfig ENV_CONFIG = ConfigsProvider.getEnvConfig();
    protected final String BASE_URL = ENV_CONFIG.getAppUrl();
    protected final DriverWaitConfiguration DRIVER_WAIT_CONFIG;

    protected BasePage(WebDriver driver) {
        this.DRIVER = driver;
        PageFactory.initElements(DRIVER, this);
        this.DRIVER_WAIT_CONFIG = new DriverWaitConfiguration(DRIVER);
    }

    public String getPageUrl() {
        return BASE_URL;
    }

    public void goTo() {
        DRIVER.get(BASE_URL);
    }

    /**
     * Decorator method that waits until the element is visible
     * and returns provided element back
     * Re-Initializes the page if element is stale and retries
     *
     * @param element - WebElement
     * @return - provided Element
     */
    protected WebElement getVisibleElement(WebElement element) {
        try {
            DRIVER_WAIT_CONFIG.waitForElement(WaitCondition.VISIBLE, element);
        } catch (StaleElementReferenceException e) {
            PageFactory.initElements(DRIVER, this);
            DRIVER_WAIT_CONFIG.waitForElement(WaitCondition.VISIBLE, element);
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
            DRIVER_WAIT_CONFIG.waitForElement(WaitCondition.CLICKABLE, element);
        } catch (StaleElementReferenceException e) {
            PageFactory.initElements(DRIVER, this);
            DRIVER_WAIT_CONFIG.waitForElement(WaitCondition.CLICKABLE, element);
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
    protected List<WebElement> getVisibleElements(List<WebElement> elements) {
        try {
            DRIVER_WAIT_CONFIG.waitForElements(WaitCondition.VISIBLE, elements);
        } catch (StaleElementReferenceException e) {
            PageFactory.initElements(DRIVER, this);
            DRIVER_WAIT_CONFIG.waitForElements(WaitCondition.VISIBLE, elements);
        }
        return elements;
    }

    /**
     * Decorator method that waits until element is clickable and clicks it
     *
     * @param element - WebElement to click
     */
    protected void waitAndClickElement(WebElement element) {
        getClickableElement(element).click();
    }

    /**
     * Decorator method that waits until provided input element is clickable
     * and enters provided value in it
     *
     * @param value - value to enter
     * @param input - input WebElement
     */
    protected void waitAndEnterValueToInput(String value, WebElement input) {
        getClickableElement(input);
        input.click();
        input.clear();
        input.sendKeys(Keys.CONTROL + "A");
        input.sendKeys(Keys.BACK_SPACE);
        input.sendKeys(value);
    }

    /**
     * Decorator method that waits until provided select WebElement is clickable
     * and selects provided value in it
     *
     * @param value  - value to select
     * @param select - select type WebElement
     */
    protected void waitAndSelectValueInSelect(String value, WebElement select) {
        new Select(getClickableElement(select)).selectByValue(value);
    }
}
