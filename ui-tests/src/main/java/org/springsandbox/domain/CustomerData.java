package org.springsandbox.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springsandbox.enums.DriverType;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerData {
    @JsonProperty("driverType")
    private DriverType driverType;
    private Set<Customer> customers;
}
