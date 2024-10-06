package org.springsandbox.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EnvConfig {
    @JsonProperty("hubUrl")
    private String hubUrl;
    @JsonProperty("selenoidEnabled")
    private Boolean selenoidEnabled;
    @JsonProperty("selenoidVncEnabled")
    private Boolean selenoidVncEnabled;
    @JsonProperty("selenoidVideoEnabled")
    private Boolean selenoidVideoEnabled;
    @JsonProperty("appUrl")
    private String appUrl;
}
