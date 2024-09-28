package utils;

import io.qameta.allure.Allure;
import org.slf4j.Logger;

import java.util.concurrent.Callable;

public class TestStep {

    public static <T> T step(String step, Logger logger, Callable<T> lambda) throws Exception {
        Allure.step(step);
        logger.info(step);
        return lambda.call();
    }

    public static void step(String step, Logger logger, Runnable action) {
        Allure.step(step);
        logger.info(step);
        action.run();
    }

    public static void step(String step, Logger logger) {
        Allure.step(step);
        logger.info(step);
    }
}
