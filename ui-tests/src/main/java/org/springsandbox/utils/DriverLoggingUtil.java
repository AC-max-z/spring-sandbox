package org.springsandbox.utils;

import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility class for handling WebDriver logging
 */
public class DriverLoggingUtil {

    /**
     * Since it is never quite straight-forward with Selenium...
     * This method accepts 2 parameters:
     * - Logger instance,
     * - Selenium Logs object, collected by WebDriver instance.
     * ...
     * Then it collects LogEntries of DRIVER, BROWSER and PERFORMANCE types from Logs object
     * to a list, sorted by LogEntry timestamp.
     * It then outputs collected LogEntries' contents using provided Logger.
     * Finally, it returns list of String representations of LogEntries' contents.
     *
     * @param logger - Logger instance
     * @param logs   - Logs, collected by WebDriver instance
     * @return - List of WebDriver LogEntries' contents as Strings, sorted by Timestamp
     */
    public static List<String> outputAndGetLogsAsStrings(Logger logger, Logs logs) {
        var logEntries = getSortedLogEntriesFromLogs(logs);
        outputLogEntries(logger, logEntries);
        return getLogEntriesAsStrings(logEntries);
    }

    /**
     * Returns LogEntries' contents as Strings
     *
     * @param logEntries - List of WebDriver LogEntry objects
     * @return List of Strings, representing provided LogEntries'
     */
    public static List<String> getLogEntriesAsStrings(List<LogEntry> logEntries) {
        // return list of Strings with LogEntries' contents
        return logEntries
                .stream()
                .map(logEntry ->
                        LocalDate.ofEpochDay(logEntry.getTimestamp()) + ":"
                                + "[" + logEntry.getLevel().getName() + "]"
                                + logEntry.getMessage() + "\n"
                )
                .toList();
    }

    /**
     * Returns List of WebDriver LogEntries sorted by timestamp from WebDriver Logs obj
     *
     * @param logs - WebDriver logs
     * @return List of WebDriver LogEntry objects, sorted by timestamp
     */
    public static List<LogEntry> getSortedLogEntriesFromLogs(Logs logs) {
        // extract LogEntries from Logs object param
        List<LogEntry> driverLogEntries = logs.get(LogType.DRIVER).getAll();
        List<LogEntry> browserLogEntries = logs.get(LogType.BROWSER).getAll();
        List<LogEntry> perfLogEntries = logs.get(LogType.PERFORMANCE).getAll();

        // flatmap and sort by timestamp
        return Stream.of(driverLogEntries, browserLogEntries, perfLogEntries)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingLong(LogEntry::getTimestamp))
                .toList();
    }

    /**
     * Outputs driver log entries using provided logger
     *
     * @param logger     logger
     * @param logEntries List of WebDriver LogEntry objects
     */
    public static void outputLogEntries(Logger logger, List<LogEntry> logEntries) {
        // Log them
        logEntries.forEach(logEntry -> {
            switch (logEntry.getLevel().getName()) {
                case "INFO":
                    logger.info(
                            "{}:[{}]{}",
                            LocalDate.ofEpochDay(logEntry.getTimestamp()),
                            logEntry.getLevel().getName(),
                            logEntry.getMessage()
                    );
                    break;
                case "WARNING", "SEVERE":
                    logger.warn(
                            "{}:[{}]{}",
                            LocalDate.ofEpochDay(logEntry.getTimestamp()),
                            logEntry.getLevel().getName(),
                            logEntry.getMessage()
                    );
                    break;
                default:
                    logger.debug(
                            "{}:[{}]{}",
                            LocalDate.ofEpochDay(logEntry.getTimestamp()),
                            logEntry.getLevel().getName(),
                            logEntry.getMessage()
                    );
                    break;
            }
        });
    }
}
