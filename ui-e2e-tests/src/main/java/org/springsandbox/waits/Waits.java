package org.springsandbox.waits;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springsandbox.enums.WaitCondition;
import org.springsandbox.util.AppProperties;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class Waits {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Map<String, String> envVars = AppProperties.getProperties();
    private final Integer implicitWait = Integer.parseInt(envVars.get("DRIVER_IMPLICIT_WAIT_MILLIS"));

    public Waits(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
        int waitTimeout = Integer.parseInt(envVars.get("DRIVER_WAIT_ELEMENT_TIMEOUT"));
        this.wait = new WebDriverWait(this.driver, Duration.ofMillis(waitTimeout));
        this.driver.manage().timeouts().implicitlyWait(Duration.ofMillis(implicitWait));
    }

    @FindBy(xpath = "//span[contains(text(), 'Loading')]")
    private WebElement loadingSpinner;

    public void waitForPageLoading() {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete")
        );
        wait.until(ExpectedConditions.invisibilityOf(loadingSpinner));
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(implicitWait));
    }

    public void waitForElement(WaitCondition condition, WebElement element) {
        waitForPageLoading();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
        switch (condition) {
            case VISIBLE -> wait
                    .until(ExpectedConditions.visibilityOf(element));
            case CLICKABLE -> wait
                    .until(ExpectedConditions.elementToBeClickable(element));
            case INVISIBLE -> wait
                    .until(ExpectedConditions.invisibilityOf(element));
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(implicitWait));
    }

    public void waitForElements(WaitCondition condition, List<WebElement> elements) {
        waitForPageLoading();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
        switch (condition) {
            case VISIBLE -> wait
                    .until(ExpectedConditions.visibilityOfAllElements(elements));
            case INVISIBLE -> wait
                    .until(ExpectedConditions.invisibilityOfAllElements(elements));
            default -> throw new IllegalArgumentException("Unexpected value: " + condition);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(implicitWait));
    }
}
