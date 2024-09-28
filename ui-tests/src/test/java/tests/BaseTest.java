package tests;

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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Objects;

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith({ScreenshotExtension.class})
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
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        if (Objects.nonNull(DRIVER_THREAD_LOCAL.get())) {
            Allure.step("Collect driver logs");
            Allure.addAttachment(
                    "driver_logs_" + LocalDate.ofEpochDay(System.currentTimeMillis()),
                    "text",
                    String.valueOf(DriverLoggingUtil.outputAndGetLogsAsStrings(
                            LOGGER_THREAD_LOCAL.get(),
                            DRIVER_THREAD_LOCAL.get().manage().logs())
                    ),
                    ".log"
            );
            Allure.step("Close driver");
            DRIVER_THREAD_LOCAL.get().quit();
        }
    }

    protected WebDriver setupDriver(DriverType driverType)
            throws MalformedURLException, URISyntaxException {
        DRIVER_THREAD_LOCAL.set(WebDriverFactory.getDriver(driverType));
        ScreenshotExtension.setDriver(DRIVER_THREAD_LOCAL.get());
        return DRIVER_THREAD_LOCAL.get();
    }
}
