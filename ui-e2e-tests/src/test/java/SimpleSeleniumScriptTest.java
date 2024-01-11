import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springsandbox.enums.DriverType;
import org.springsandbox.factories.WebDriverFactory;
import org.springsandbox.pages.IndexPage;

import static org.assertj.core.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;

@Execution(ExecutionMode.CONCURRENT)
public class SimpleSeleniumScriptTest {

    ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @BeforeAll
    static void beforeAll() {
    }

    @AfterEach
    void tearDown() {
        if (Objects.nonNull(driverThreadLocal.get())) {
            driverThreadLocal.get().quit();
        }
    }

    @ParameterizedTest
    @EnumSource(DriverType.class)
    public void test1(DriverType driverType) throws MalformedURLException, URISyntaxException {
        driverThreadLocal.set(WebDriverFactory.getDriver(driverType));
        var driver = driverThreadLocal.get();

        var indexPage = new IndexPage(driver);
        indexPage.goTo();
        List<WebElement> customerCards = indexPage.getCustomerCards();
        assertThat(customerCards).hasSize(9);
    }
}
