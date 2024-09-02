package org.springsandbox.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.rnorth.ducttape.unreliables.Unreliables;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class IndexPage extends BasePage {

    public IndexPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//span[contains(text(), 'Loading')]")
    private WebElement loadingSpinner;

    @FindAll(@FindBy(xpath = "//li[contains(@class, 'chakra-wrap__listitem')]"))
    private List<WebElement> customerCards;

    @FindBy(xpath = "//button[contains(text(), 'Create customer')]")
    private WebElement createCustomerButton;

    @FindBy(xpath = "//footer[contains(@class, 'chakra-modal__footer')]/button[text()='Delete']")
    private WebElement confirmDeleteButton;

    public List<WebElement> getCustomerCards() {
        return getElements(customerCards);
    }

    public WebElement getCustomerCardWithEmail(String email) {
        return getCustomerCards().stream()
                .filter(webEl -> webEl
                        .findElement(By.xpath(".//p[contains(text(), '@')]"))
                        .getText()
                        .equals(email))
                .findFirst()
                .orElse(null);
    }

    public String getCustomerNameFromCard(WebElement customerCard) {
        getElement(customerCard);
        return customerCard
                .findElement(By.xpath(".//h2"))
                .getText();
    }

    public Integer getCustomerAgeFromCard(WebElement customerCard) {
        getElement(customerCard);
        return Integer
                .parseInt(customerCard
                        .findElement(By.xpath(".//p/span"))
                        .getText()
                        .split(" ")[1]
                );
    }

    public String getCustomerGenderFromCard(WebElement customerCard) {
        getElement(customerCard);
        return customerCard
                .findElement(By.xpath(".//span[contains(@class, 'chakra-badge')]"))
                .getText();
    }

    public WebElement getCreateCustomerButton() {
        return getElement(createCustomerButton);
    }

    public void clickCreateCustomerButton() {
        clickElement(createCustomerButton);
    }

    public void clickDeleteCustomer(WebElement customerCard) {
        WebElement deleteButton = customerCard.findElement(By.xpath(".//button[text()='Delete']"));
        // TODO: this is done with duct tape because success toast in displayed on top of delete button
        //  and prevents from clicking it until it disappears
        //  Solution 0 (shitty): scroll down a bit
        //  Solution 1 (ok): close toast
        //  CBA doing any of them atm so...
        Unreliables.retryUntilSuccess(10, TimeUnit.SECONDS, () -> {
            clickElement(deleteButton);
            return null;
        });
    }

    public void clickEditCustomer(WebElement customerCard) {
        WebElement editButton = customerCard.findElement(By.xpath(".//button[text()='Edit']"));
        Unreliables.retryUntilSuccess(10, TimeUnit.SECONDS, () -> {
            clickElement(editButton);
            return null;
        });
    }

    public void confirmDeleteCustomer() {
        clickElement(confirmDeleteButton);
    }

}
