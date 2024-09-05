package org.springsandbox.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerTestDataYaml {
    @JsonProperty("data")
    private Set<CustomerData> data;
}
