package util;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.ByteArrayInputStream;
import java.io.File;

public class ScreenshotExtension implements AfterTestExecutionCallback {
    private static WebDriver driver;

    public static void setDriver(WebDriver driver) {
        ScreenshotExtension.driver = driver;
    }

    @Override
    public void afterTestExecution(ExtensionContext ctx) throws Exception {
        if (ctx.getExecutionException().isPresent()) {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(System.getProperty("user.dir" + "/screenshots"
                    + ctx.getDisplayName() + ".jpg"));
            FileHandler.copy(src, dest);
            Allure.addAttachment("Screenshot", new ByteArrayInputStream(
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)
            ));
        }
    }
}
