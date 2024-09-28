package org.springsandbox.config.test_data.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springsandbox.domain.Customer;
import org.springsandbox.enums.DriverType;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerTestDataEntry {
    @JsonProperty("driverType")
    private DriverType driverType;
    @JsonProperty("customers")
    private Set<Customer> customers;
}
