package org.springsandbox.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springsandbox.util.AppProperties;
import org.springsandbox.enums.WaitCondition;
import org.springsandbox.waits.Waits;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexPage {
    private final WebDriver driver;
    private final Map<String, String> envVars = AppProperties.getProperties();
    private final String pageUrl = envVars.get("APP_URL");
    private final Waits waits;

    public IndexPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.waits = new Waits(driver);
    }

    @FindBy(xpath = "//span[contains(text(), 'Loading')]")
    private WebElement loadingSpinner;

    @FindAll(@FindBy(xpath = "//li[contains(@class, 'chakra-wrap__listitem')]"))
    private List<WebElement> customerCards;

    public String getPageUrl() {
        return pageUrl;
    }

    public void goTo() {
        driver.get(pageUrl);
    }

    public List<WebElement> getCustomerCards() {
        waits.waitForElements(WaitCondition.VISIBLE, customerCards);
        return customerCards;
    }

    public Map<String, WebElement> elementsMap() {
        return Stream.of(new Object[][]{
                {"Loading spinner", this.loadingSpinner}
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (WebElement) data[1]));
    }

    public Map<String, List<WebElement>> listsOfElementsMap() {
        return Stream.of(new Object[][]{
                {"Customer cards", this.customerCards}
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (List<WebElement>) data[1]));
    }
}
