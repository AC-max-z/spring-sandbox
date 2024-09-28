package org.springsandbox.config.test_data.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerTestDataYaml {
    @JsonProperty("data")
    private Set<CustomerTestDataEntry> data;
}
