package org.springsandbox.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EnvConfig {
    @JsonProperty("gridUrl")
    private String gridUrl;
    @JsonProperty("selenoidEnabled")
    private Boolean selenoidEnabled;
    @JsonProperty("selenoidVncEnabled")
    private Boolean selenoidVncEnabled;
    @JsonProperty("selenoidVideoEnabled")
    private Boolean selenoidVideoEnabled;
    @JsonProperty("appUrl")
    private String appUrl;
}
