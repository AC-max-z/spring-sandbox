package org.springsandbox.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DriverConfig {
    private Integer waitTimeoutMillis;
    private Integer implicitWaitMillis;
    private Integer pollingIntervalMillis;
    private Boolean chromeLocalLoggingEnabled;
    private String chromeLocalLogPath;
    private Boolean firefoxLocalLoggingEnabled;
    private String firefoxLocalLogPath;
}
