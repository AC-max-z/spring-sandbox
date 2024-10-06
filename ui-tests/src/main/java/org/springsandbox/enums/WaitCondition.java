package org.springsandbox.enums;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import java.util.List;

public enum WaitCondition {
    VISIBLE {
        @Override
        public void apply(Wait<WebDriver> wait, WebElement element) {
            wait.until(ExpectedConditions.visibilityOf(element));
        }

        @Override
        public void apply(Wait<WebDriver> wait, List<WebElement> elements) {
            wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        }
    },
    CLICKABLE {
        @Override
        public void apply(Wait<WebDriver> wait, WebElement element) {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        }

        @Override
        public void apply(Wait<WebDriver> wait, List<WebElement> elements) {
            throw new UnsupportedOperationException(
                    "CLICKABLE is not supported for list of elements");
        }
    },
    INVISIBLE {
        @Override
        public void apply(Wait<WebDriver> wait, WebElement element) {
            wait.until(ExpectedConditions.invisibilityOf(element));
        }

        @Override
        public void apply(Wait<WebDriver> wait, List<WebElement> elements) {
            wait.until(ExpectedConditions.invisibilityOfAllElements(elements));
        }
    };

    public abstract void apply(Wait<WebDriver> wait, WebElement element);

    public abstract void apply(Wait<WebDriver> wait, List<WebElement> elements);
}
