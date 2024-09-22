package org.springsandbox.factories;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springsandbox.config.DriverConfig;
import org.springsandbox.config.EnvConfig;
import org.springsandbox.enums.DriverType;
import org.springsandbox.util.AppConfig;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class WebDriverFactory {
    private static final DriverConfig driverConfig = AppConfig.getDriverConfig();
    private static final EnvConfig envConfig = AppConfig.getEnvConfig();

    public static WebDriver getDriver(DriverType driverType) throws URISyntaxException, MalformedURLException {
        return switch (driverType) {

            case DriverType.CHROME -> {
                ChromeDriverService service;
                if (driverConfig.getChromeLocalLoggingEnabled()) {
                    File logLocation = new File(driverConfig.getChromeLocalLogPath());
                    service = new ChromeDriverService.Builder()
                            .withLogOutput(System.out)
                            .withLogFile(logLocation)
                            .withReadableTimestamp(true)
                            .build();
                } else {
                    service = new ChromeDriverService.Builder()
                            .withLogOutput(System.out)
                            .withReadableTimestamp(true)
                            .build();
                }
                var opts = new ChromeOptions();
                yield new ChromeDriver(service, opts);
            }

            case DriverType.FIREFOX -> {
                GeckoDriverService geckoService;
                if (driverConfig.getFirefoxLocalLoggingEnabled()) {
                    File logLocation = new File(driverConfig.getFirefoxLocalLogPath());
                    geckoService =
                            new GeckoDriverService.Builder()
                                    .withLogFile(logLocation)
                                    .build();
                } else {
                    geckoService = new GeckoDriverService.Builder()
                            .withLogOutput(System.out)
                            .build();
                }
                var opts = new FirefoxOptions();
                yield new FirefoxDriver(geckoService, opts);
            }

            case DriverType.FIREFOX_REMOTE -> {
                FirefoxOptions opts = new FirefoxOptions();
                opts.setEnableDownloads(true);
                LoggingPreferences logPrefs = new LoggingPreferences();
                setLoggingPreferences(logPrefs);
                // TODO: set capability for firefox logging
                setSelenoidOptions(opts);
                yield new RemoteWebDriver(new URI(envConfig.getGridUrl()).toURL(), opts);
            }

            case DriverType.CHROME_REMOTE -> {
                ChromeOptions options = new ChromeOptions();
                options.setEnableDownloads(true);
                LoggingPreferences logPrefs = new LoggingPreferences();
                setLoggingPreferences(logPrefs);
                options.setCapability("goog:loggingPrefs", logPrefs);
                setSelenoidOptions(options);
                yield new RemoteWebDriver(new URI(envConfig.getGridUrl()).toURL(), options);
            }

            case CHROME_REMOTE_HEADLESS -> {
                var options = new ChromeOptions();
                options.setEnableDownloads(true);
                LoggingPreferences logPrefs = new LoggingPreferences();
                setLoggingPreferences(logPrefs);
                Map<String, Object> perfLogPrefs = new HashMap<>();
                perfLogPrefs.put("enableNetwork", true);
                perfLogPrefs.put("traceCategories", "devtools.network");
                options.setExperimentalOption("perfLoggingPrefs", perfLogPrefs);
                options.setCapability("goog:loggingPrefs", logPrefs);
                setSelenoidOptions(options);
                options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
                var capabilities = new DesiredCapabilities();
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                yield new RemoteWebDriver(new URI(envConfig.getGridUrl()).toURL(), capabilities);
            }

            case DriverType.FIREFOX_REMOTE_HEADLESS -> {
                var opts = new FirefoxOptions();
                opts.addArguments("-headless");
                opts.setEnableDownloads(true);
                LoggingPreferences logPrefs = new LoggingPreferences();
                setLoggingPreferences(logPrefs);
                // TODO: set capability for firefox logging
                setSelenoidOptions(opts);
                yield new RemoteWebDriver(new URI(envConfig.getGridUrl()).toURL(), opts);
            }

        };
    }

    private static void setLoggingPreferences(LoggingPreferences logPrefs) {
        logPrefs.enable(LogType.DRIVER, Level.ALL);
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
    }

    private static void setSelenoidOptions(AbstractDriverOptions<?> opts) {
        if (envConfig.getSelenoidEnabled()) {
            opts.setCapability("selenoid:options", new HashMap<String, Object>() {
                {
                    put("enableVideo", envConfig.getSelenoidVideoEnabled());
                    put("enableVNC", envConfig.getSelenoidVncEnabled());
                }
            });
        }
    }
}
