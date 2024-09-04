package org.springsandbox.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EnvConfig {
    private String gridUrl;
    private Boolean selenoidEnabled;
    private Boolean selenoidVncEnabled;
    private Boolean selenoidVideoEnabled;
    private String appUrl;
}
