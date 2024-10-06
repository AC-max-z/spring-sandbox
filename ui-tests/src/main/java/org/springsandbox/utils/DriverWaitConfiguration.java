package org.springsandbox.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springsandbox.config.ConfigsProvider;
import org.springsandbox.config.DriverConfig;
import org.springsandbox.enums.WaitCondition;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

/* Encapsulates WebDriver's waiting logic configuration */
public class DriverWaitConfiguration {
    private final WebDriver DRIVER;
    private final Wait<WebDriver> WAIT;
    private final DriverConfig DRIVER_CONFIG = ConfigsProvider.getDriverConfig();
    private final Integer IMPLICIT_WAIT_MS = DRIVER_CONFIG.getImplicitWaitMillis();

    public DriverWaitConfiguration(WebDriver driver) {
        this.DRIVER = driver;
        PageFactory.initElements(this.DRIVER, this);

        this.WAIT = new FluentWait<>(this.DRIVER)
                .withTimeout(Duration.ofMillis(DRIVER_CONFIG.getWaitTimeoutMillis()))
                .pollingEvery(Duration.ofMillis(DRIVER_CONFIG.getPollingIntervalMillis()))
                .ignoring(NoSuchElementException.class);

        this.DRIVER.manage().timeouts().implicitlyWait(Duration.ofMillis(IMPLICIT_WAIT_MS));
    }

    @FindBy(xpath = "//span[contains(text(), 'Loading')]")
    private WebElement loadingSpinner;

    /*
     * Waits for page to finish loading by checking document.readystate=complete
     * and waiting until all load spinners/skeletons on spa pages are invisible
     */
    public void waitForPageLoading() {
        temporarilyDisableImplicitWait(() -> {
            WAIT.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState")
                    .equals("complete")
            );
            WAIT.until(ExpectedConditions.invisibilityOf(loadingSpinner));
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
        temporarilyDisableImplicitWait(() -> condition.apply(WAIT, element));
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
        temporarilyDisableImplicitWait(() -> condition.apply(WAIT, elements));
    }

    /**
     * Temporarily disables implicit waits, until provided action is fulfilled
     * then enables implicit wait back
     *
     * @param action - Runnable action to execute that contains explicit waits
     */
    private void temporarilyDisableImplicitWait(Runnable action) {
        DRIVER.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
        try {
            action.run();
        } finally {
            DRIVER.manage().timeouts().implicitlyWait(Duration.ofMillis(IMPLICIT_WAIT_MS));
        }
    }
}
