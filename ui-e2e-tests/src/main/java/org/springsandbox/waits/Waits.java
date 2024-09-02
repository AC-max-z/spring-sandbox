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

public class Waits {
    private final WebDriver driver;
    private final Wait<WebDriver> wait;
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

    public void waitForPageLoading() {
        temporarilyDisableImplicitWait(() -> {
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState")
                    .equals("complete")
            );
            wait.until(ExpectedConditions.invisibilityOf(loadingSpinner));
        });
    }

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

    private void temporarilyDisableImplicitWait(Runnable action) {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
        try {
            action.run();
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(implicitWait));
        }
    }
}
