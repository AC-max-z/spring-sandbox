package util;

import io.qameta.allure.Allure;
import org.slf4j.Logger;

public class Step {

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
