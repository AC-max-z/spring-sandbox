package extensions;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.Objects;

/* Attaches screenshot to report if exception is thrown after test execution */
public class ScreenshotExtension implements AfterTestExecutionCallback {
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    @Override
    public void afterTestExecution(ExtensionContext ctx) {
        if (Objects.nonNull(DRIVER_THREAD_LOCAL.get())) {
            try {
                if (ctx.getExecutionException().isPresent()) {
                    Allure.addAttachment(
                            "scr_" + Instant.ofEpochMilli(System.currentTimeMillis()),
                            "picture",
                            new ByteArrayInputStream(((TakesScreenshot) DRIVER_THREAD_LOCAL.get())
                                    .getScreenshotAs(OutputType.BYTES)),
                            ".png"
                    );
                }
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }
}
