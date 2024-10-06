package org.springsandbox.enums;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springsandbox.factories.WebDriverFactory;

public enum DriverType {

    CHROME {
        @Override
        public WebDriver createDriver(String browserVersion) {
            ChromeDriverService service = WebDriverFactory.getChromeService();
            ChromeOptions options = WebDriverFactory.getChromeOptions(browserVersion);
            return new ChromeDriver(service, options);
        }
    },

    FIREFOX {
        @Override
        public WebDriver createDriver(String browserVersion) {
            GeckoDriverService service = WebDriverFactory.getGeckoService();
            FirefoxOptions options = WebDriverFactory.getFirefoxOptions(browserVersion);
            return new FirefoxDriver(service, options);
        }
    },

    CHROME_REMOTE {
        @Override
        public WebDriver createDriver(String browserVersion) {
            ChromeOptions options = WebDriverFactory.getChromeOptions(browserVersion);
            return new RemoteWebDriver(WebDriverFactory.getHUB_URL(), options);
        }
    },

    FIREFOX_REMOTE {
        @Override
        public WebDriver createDriver(String browserVersion) {
            FirefoxOptions options = WebDriverFactory.getFirefoxOptions(browserVersion);
            return new RemoteWebDriver(WebDriverFactory.getHUB_URL(), options);
        }
    },

    CHROME_REMOTE_HEADLESS {
        @Override
        public WebDriver createDriver(String browserVersion) {
            ChromeOptions options = WebDriverFactory.getChromeOptions(browserVersion);
            options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            return new RemoteWebDriver(WebDriverFactory.getHUB_URL(), capabilities);
        }
    },

    FIREFOX_REMOTE_HEADLESS {
        @Override
        public WebDriver createDriver(String browserVersion) {
            FirefoxOptions options = WebDriverFactory.getFirefoxOptions(browserVersion);
            options.addArguments("-headless");
            return new RemoteWebDriver(WebDriverFactory.getHUB_URL(), options);
        }
    };

    public abstract WebDriver createDriver(String browserVersion);
}