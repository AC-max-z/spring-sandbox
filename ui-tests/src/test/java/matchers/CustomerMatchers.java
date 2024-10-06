package matchers;

import org.openqa.selenium.WebElement;
import org.springsandbox.domain.Customer;
import org.springsandbox.pages.IndexPage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class CustomerMatchers {

    public static void verifyCustomerCardContainsCustomerData(
            IndexPage indexPage,
            WebElement customerCard,
            Customer customer
    ) {
        assertThat(indexPage.getCustomerNameFromCard(customerCard)).isEqualTo(customer.getName());
        assertThat(indexPage.getCustomerAgeFromCard(customerCard)).isEqualTo(customer.getAge());
        assertThat(indexPage.getCustomerGenderFromCard(customerCard)).isEqualTo(customer.getGender());
    }

    public static void verifyCustomerCardContainsCustomerDataSoftly(
            IndexPage indexPage,
            WebElement customerCard,
            Customer customer
    ) {
        assertSoftly(softly -> {
            softly.assertThat(indexPage.getCustomerNameFromCard(customerCard)).isEqualTo(customer.getName());
            softly.assertThat(indexPage.getCustomerAgeFromCard(customerCard)).isEqualTo(customer.getAge());
            softly.assertThat(indexPage.getCustomerGenderFromCard(customerCard)).isEqualTo(customer.getGender());
        });
    }
}
