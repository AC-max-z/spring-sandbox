package extensions;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Objects;

/* Logs exception message and stacktrace if exception is thrown after test execution */
public class ExceptionLoggerExtension implements AfterTestExecutionCallback {
    private static final ThreadLocal<Logger> LOGGER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setLogger(Logger logger) {
        LOGGER_THREAD_LOCAL.set(logger);
    }

    @Override
    public void afterTestExecution(ExtensionContext ctx) {
        if (ctx.getExecutionException().isPresent() &&
                Objects.nonNull(LOGGER_THREAD_LOCAL.get())) {
            var logger = LOGGER_THREAD_LOCAL.get();
            logger.error(ctx.getExecutionException().get().getLocalizedMessage());
            logger.error(Arrays.toString(ctx.getExecutionException().get().getStackTrace()));
        }
        LOGGER_THREAD_LOCAL.remove();
    }
}
