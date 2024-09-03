package org.springsandbox.waits;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springsandbox.enums.WaitCondition;
import org.springsandbox.util.AppProperties;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Encapsulates the waiting logic
 */
public class Waits {
    private final WebDriver driver;
    private final Wait<WebDriver> wait;
    // Environment variables and properties from application.properties
    private final Map<String, String> envVars = AppProperties.getProperties();
    private final Integer implicitWait = Integer.parseInt(envVars.get("DRIVER_IMPLICIT_WAIT_MILLIS"));

    public Waits(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);

        int waitTimeout = Integer.parseInt(envVars.get("DRIVER_WAIT_ELEMENT_TIMEOUT"));
        int pollingInterval = Integer.parseInt(envVars.get("DRIVER_POLLING_INTERVAL_MILLIS"));

        this.wait = new FluentWait<>(this.driver)
                .withTimeout(Duration.ofMillis(waitTimeout))
                .pollingEvery(Duration.ofMillis(pollingInterval))
                .ignoring(NoSuchElementException.class);

        this.driver.manage().timeouts().implicitlyWait(Duration.ofMillis(implicitWait));
    }

    @FindBy(xpath = "//span[contains(text(), 'Loading')]")
    private WebElement loadingSpinner;

    /**
     * Waits for page to finish loading by checking document.readystate=complete
     * and waiting until all load spinners/skeletons on spa pages are invisible
     */
    public void waitForPageLoading() {
        temporarilyDisableImplicitWait(() -> {
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState")
                    .equals("complete")
            );
            wait.until(ExpectedConditions.invisibilityOf(loadingSpinner));
        });
    }

    /**
     * Wait until specified condition is met for the provided element
     * (waits for page to finish loading first and disables implicit waits
     * while waiting, when condition is met - enables it back)
     *
     * @param condition - condition to be met
     * @param element   - WebElement
     */
    public void waitForElement(WaitCondition condition, WebElement element) {
        waitForPageLoading();
        temporarilyDisableImplicitWait(() -> {
            switch (condition) {
                case VISIBLE -> wait
                        .until(ExpectedConditions.visibilityOf(element));
                case CLICKABLE -> wait
                        .until(ExpectedConditions.elementToBeClickable(element));
                case INVISIBLE -> wait
                        .until(ExpectedConditions.invisibilityOf(element));
            }
        });
    }

    /**
     * Wait until specified condition is met for provided elements
     * (waits for page to finish loading first and disables implicit waits
     * while waiting, when condition is met - enables it back)
     *
     * @param condition - condition to be met
     * @param elements  - List of WebElements
     */
    public void waitForElements(WaitCondition condition, List<WebElement> elements) {
        waitForPageLoading();
        temporarilyDisableImplicitWait(() -> {
            switch (condition) {
                case VISIBLE -> wait
                        .until(ExpectedConditions.visibilityOfAllElements(elements));
                case INVISIBLE -> wait
                        .until(ExpectedConditions.invisibilityOfAllElements(elements));
                default -> throw new IllegalArgumentException("Unexpected value: " + condition);
            }
        });
    }

    /**
     * Temporarily disables implicit waits, until provided action is fulfilled
     * then enables implicit wait back
     *
     * @param action - Runnable action to execute that contains explicit waits
     */
    private void temporarilyDisableImplicitWait(Runnable action) {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
        try {
            action.run();
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(implicitWait));
        }
    }
}
