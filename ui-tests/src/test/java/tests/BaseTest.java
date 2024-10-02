package tests;

import extensions.ExceptionLoggerExtension;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springsandbox.enums.DriverType;
import org.springsandbox.factories.WebDriverFactory;
import org.springsandbox.utils.DriverLoggingUtil;
import extensions.ScreenshotExtension;

import java.time.Instant;
import java.util.Objects;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith({
        ScreenshotExtension.class,
        ExceptionLoggerExtension.class
})
@Tags({
        @Tag("UI"),
        @Tag("E2E")
})
public abstract class BaseTest {
    protected final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();
    protected final ThreadLocal<Logger> LOGGER_THREAD_LOCAL = new ThreadLocal<>();

    @BeforeEach
    void setUp(TestInfo testInfo) {
        LOGGER_THREAD_LOCAL.set(LoggerFactory.getLogger(
                testInfo.getTestClass().get().getName()));
        ExceptionLoggerExtension.setLogger(LOGGER_THREAD_LOCAL.get());
    }

    @AfterEach
    void tearDown() {
        if (Objects.nonNull(DRIVER_THREAD_LOCAL.get())) {
            try {
                Allure.step("Collect driver logs");
                Allure.addAttachment(
                        "driver_logs_" + Instant.ofEpochMilli(System.currentTimeMillis()),
                        "text",
                        String.valueOf(DriverLoggingUtil.outputAndGetLogsAsStrings(
                                LOGGER_THREAD_LOCAL.get(),
                                DRIVER_THREAD_LOCAL.get().manage().logs())
                        ),
                        ".log"
                );
            } finally {
                Allure.step("Close driver");
                DRIVER_THREAD_LOCAL.get().quit();
                DRIVER_THREAD_LOCAL.remove();
            }
        }
        LOGGER_THREAD_LOCAL.remove();
    }

    protected WebDriver setupDriver(DriverType driverType) {
        return setupDriver(driverType, "latest");
    }

    protected WebDriver setupDriver(DriverType driverType, String browserVersion) {
        DRIVER_THREAD_LOCAL.set(WebDriverFactory.getDriver(driverType, browserVersion));
        ScreenshotExtension.setDriver(DRIVER_THREAD_LOCAL.get());
        return DRIVER_THREAD_LOCAL.get();
    }
}
