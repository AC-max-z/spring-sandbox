package org.springsandbox.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Customer {
    private String name;
    private String email;
    private int age;
    private String gender;
}
