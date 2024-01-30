package org.springsandbox.factories;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springsandbox.enums.DriverType;
import org.springsandbox.util.AppProperties;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {
    private static final Map<String, String> envVars = AppProperties.getProperties();
    private static final String gridUrl = envVars.get("GRID_URL");
    public static WebDriver getDriver(DriverType driverType) throws URISyntaxException, MalformedURLException {
        return switch (driverType) {

            case DriverType.CHROME -> {
                File logLocation = new File("src/test/resources/logs/chrome.log");
                ChromeDriverService service =
                        new ChromeDriverService.Builder()
                                .withLogOutput(System.out)
                                .withLogFile(logLocation)
                                .withReadableTimestamp(true)
                                .build();
                var opts = new ChromeOptions();
                yield new ChromeDriver(service, opts);
            }

            case DriverType.FIREFOX -> {
                File logLocation = new File("src/test/resources/logs/firefox.log");
                GeckoDriverService geckoService =
                        new GeckoDriverService.Builder()
                                .withLogFile(logLocation)
//                                .withLogOutput(System.out)
                                .build();
                var opts = new FirefoxOptions();
                yield new FirefoxDriver(geckoService, opts);
            }

            case DriverType.FIREFOX_REMOTE -> {
                FirefoxOptions opts = new FirefoxOptions();
                opts.setEnableDownloads(true);
                opts.setCapability("selenoid:options", new HashMap<String, Object>() {
                    {
//                        put("enableVideo", true);
                        put("enableVNC", true);
                    }
                });
                yield new RemoteWebDriver(new URI(gridUrl).toURL(), opts);
            }

            case DriverType.CHROME_REMOTE -> {
                ChromeOptions options = new ChromeOptions();
                options.setEnableDownloads(true);
                options.setCapability("selenoid:options", new HashMap<String, Object>() {
                    {
//                        put("enableVideo", true);
                        put("enableVNC", true);
                    }
                });
                yield new RemoteWebDriver(new URI(gridUrl).toURL(), options);
            }

        };
    }
}
