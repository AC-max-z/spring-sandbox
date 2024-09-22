package util;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.slf4j.Logger;

import java.util.List;

public class DriverLogger {
    public static void log(Logger logger, Logs logs) {
        List<LogEntry> driverLogEntries = logs.get(LogType.DRIVER).getAll();
        List<LogEntry> browserLogEntries = logs.get(LogType.BROWSER).getAll();
        List<LogEntry> perfLogEntries = logs.get(LogType.PERFORMANCE).getAll();
        driverLogEntries.forEach(logEntry -> logger.info(logEntry.getMessage()));
        browserLogEntries.forEach(logEntry -> logger.info(logEntry.getMessage()));
        perfLogEntries.forEach(logEntry -> logger.info(logEntry.getMessage()));
    }
}
