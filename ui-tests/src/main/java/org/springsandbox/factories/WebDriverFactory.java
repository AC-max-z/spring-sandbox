package org.springsandbox.factories;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
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
import org.springsandbox.utils.Configs;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class WebDriverFactory {
    private static final DriverConfig DRIVER_CONFIG = Configs.getDriverConfig();
    private static final EnvConfig ENV_CONFIG = Configs.getEnvConfig();
    private static final URL GRID_URL;

    static {
        try {
            GRID_URL = new URI(ENV_CONFIG.getGridUrl()).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static WebDriver getDriver(DriverType driverType) {
        return getDriver(driverType, "latest");
    }

    public static WebDriver getDriver(DriverType driverType, String browserVersion) {
        return switch (driverType) {

            case DriverType.CHROME -> {
                var service = getChromeService();
                var opts = getChromeOptions();
                opts.setBrowserVersion(browserVersion);
                yield new ChromeDriver(service, opts);
            }

            case DriverType.FIREFOX -> {
                var geckoService = getGeckoService();
                var opts = getFirefoxOptions();
                opts.setBrowserVersion(browserVersion);
                yield new FirefoxDriver(geckoService, opts);
            }

            case DriverType.FIREFOX_REMOTE -> {
                var opts = getFirefoxOptions();
                opts.setBrowserVersion(browserVersion);
                yield new RemoteWebDriver(GRID_URL, opts);
            }

            case DriverType.CHROME_REMOTE -> {
                var opts = getChromeOptions();
                opts.setBrowserVersion(browserVersion);
                yield new RemoteWebDriver(GRID_URL, opts);
            }

            case CHROME_REMOTE_HEADLESS -> {
                var opts = getChromeOptions();
                opts.setBrowserVersion(browserVersion);
                opts.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
                var capabilities = new DesiredCapabilities();
                capabilities.setCapability(ChromeOptions.CAPABILITY, opts);
                yield new RemoteWebDriver(GRID_URL, capabilities);
            }

            case DriverType.FIREFOX_REMOTE_HEADLESS -> {
                var opts = getFirefoxOptions();
                opts.setBrowserVersion(browserVersion);
                opts.addArguments("-headless");
                yield new RemoteWebDriver(GRID_URL, opts);
            }

        };
    }

    private static ChromeOptions getChromeOptions() {
        var opts = new ChromeOptions();
        setGenericDriverOptions(opts);

        var logPrefs = getLoggingPreferences();
        opts.setCapability("goog:loggingPrefs", logPrefs);

        // add perf logging options for network
        Map<String, Object> perfLogPrefs = new HashMap<>();
        perfLogPrefs.put("enableNetwork", true);
        perfLogPrefs.put("traceCategories", "devtools.network");
        opts.setExperimentalOption("perfLoggingPrefs", perfLogPrefs);

        setSelenoidOptions(opts);

        return opts;
    }

    private static FirefoxOptions getFirefoxOptions() {
        var opts = new FirefoxOptions();
        setGenericDriverOptions(opts);

        opts.setLogLevel(FirefoxDriverLogLevel.TRACE);

        setSelenoidOptions(opts);

        return opts;
    }

    private static void setGenericDriverOptions(AbstractDriverOptions<?> opts) {
        opts.setEnableDownloads(true);
        opts.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        opts.setPageLoadTimeout(Duration.ofMillis(DRIVER_CONFIG.getPageLoadTimeoutMillis()));
        opts.setAcceptInsecureCerts(false);
        opts.setImplicitWaitTimeout(Duration.ofMillis(DRIVER_CONFIG.getImplicitWaitMillis()));
    }

    private static ChromeDriverService getChromeService() {
        if (DRIVER_CONFIG.getChromeLocalLoggingEnabled()) {
            var logFile = new File(DRIVER_CONFIG.getChromeLocalLogPath() +
                    "/chrome_" + Instant.ofEpochMilli(System.currentTimeMillis()) + ".log");
            return new ChromeDriverService.Builder()
                    .withLogOutput(System.out)
                    .withLogFile(logFile)
                    .withReadableTimestamp(true)
                    .build();
        }
        return new ChromeDriverService.Builder()
                .withLogOutput(System.out)
                .withReadableTimestamp(true)
                .build();
    }

    private static GeckoDriverService getGeckoService() {
        if (DRIVER_CONFIG.getFirefoxLocalLoggingEnabled()) {
            var logFile = new File(DRIVER_CONFIG.getFirefoxLocalLogPath() +
                    "/firefox_" + Instant.ofEpochMilli(System.currentTimeMillis()) + ".log");
            return new GeckoDriverService.Builder()
                    .withLogFile(logFile)
                    .build();
        }
        return new GeckoDriverService.Builder()
                .withLogOutput(System.out)
                .build();
    }

    private static LoggingPreferences getLoggingPreferences() {
        var logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.DRIVER, Level.ALL);
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        return logPrefs;
    }

    private static void setSelenoidOptions(AbstractDriverOptions<?> opts) {
        if (ENV_CONFIG.getSelenoidEnabled()) {
            opts.setCapability("selenoid:options", new HashMap<String, Object>() {
                {
                    put("enableVideo", ENV_CONFIG.getSelenoidVideoEnabled());
                    put("enableVNC", ENV_CONFIG.getSelenoidVncEnabled());
                }
            });
        }
    }
}
