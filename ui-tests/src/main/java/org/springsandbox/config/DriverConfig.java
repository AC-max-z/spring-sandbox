package org.springsandbox.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DriverConfig {
    @JsonProperty("waitTimeoutMillis")
    private Integer waitTimeoutMillis;
    @JsonProperty("implicitWaitMillis")
    private Integer implicitWaitMillis;
    @JsonProperty("pollingIntervalMillis")
    private Integer pollingIntervalMillis;
    @JsonProperty("chromeLocalLoggingEnabled")
    private Boolean chromeLocalLoggingEnabled;
    @JsonProperty("chromeLocalLogPath")
    private String chromeLocalLogPath;
    @JsonProperty("firefoxLocalLoggingEnabled")
    private Boolean firefoxLocalLoggingEnabled;
    @JsonProperty("firefoxLocalLogPath")
    private String firefoxLocalLogPath;
    @JsonProperty("pageLoadTimeoutMillis")
    private Integer pageLoadTimeoutMillis;
}
