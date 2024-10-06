package org.springsandbox.factories;

import lombok.Getter;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.springsandbox.config.DriverConfig;
import org.springsandbox.config.EnvConfig;
import org.springsandbox.enums.DriverType;
import org.springsandbox.config.ConfigsProvider;

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
    private static final DriverConfig DRIVER_CONFIG = ConfigsProvider.getDriverConfig();
    private static final EnvConfig ENV_CONFIG = ConfigsProvider.getEnvConfig();
    @Getter
    private static final URL HUB_URL;

    static {
        try {
            HUB_URL = new URI(ENV_CONFIG.getHubUrl()).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static WebDriver getDriver(DriverType driverType) {
        return getDriver(driverType, "latest");
    }

    public static WebDriver getDriver(DriverType driverType, String browserVersion) {
        return driverType.createDriver(browserVersion);
    }

    public static ChromeOptions getChromeOptions(String browserVersion) {
        var opts = new ChromeOptions();
        setGenericDriverOptions(opts, browserVersion);
        setSelenoidOptions(opts);

        var logPrefs = getLoggingPreferences();
        opts.setCapability("goog:loggingPrefs", logPrefs);

        // add perf logging options for network
        Map<String, Object> perfLogPrefs = new HashMap<>();
        perfLogPrefs.put("enableNetwork", true);
        perfLogPrefs.put("traceCategories", "devtools.network");
        opts.setExperimentalOption("perfLoggingPrefs", perfLogPrefs);

        return opts;
    }

    public static FirefoxOptions getFirefoxOptions(String browserVersion) {
        var opts = new FirefoxOptions();
        setGenericDriverOptions(opts, browserVersion);
        setSelenoidOptions(opts);

        opts.setLogLevel(FirefoxDriverLogLevel.TRACE);

        return opts;
    }

    private static void setGenericDriverOptions(AbstractDriverOptions<?> opts, String browserVersion) {
        opts.setEnableDownloads(true);
        opts.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        opts.setPageLoadTimeout(Duration.ofMillis(DRIVER_CONFIG.getPageLoadTimeoutMillis()));
        opts.setAcceptInsecureCerts(false);
        opts.setImplicitWaitTimeout(Duration.ofMillis(DRIVER_CONFIG.getImplicitWaitMillis()));
        opts.setBrowserVersion(browserVersion);
    }

    public static ChromeDriverService getChromeService() {
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

    public static GeckoDriverService getGeckoService() {
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
