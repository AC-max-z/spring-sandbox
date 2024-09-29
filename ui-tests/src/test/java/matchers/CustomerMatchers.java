package matchers;

import org.openqa.selenium.WebElement;
import org.springsandbox.domain.Customer;
import org.springsandbox.pages.IndexPage;

import static org.assertj.core.api.Assertions.assertThat;

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
}
