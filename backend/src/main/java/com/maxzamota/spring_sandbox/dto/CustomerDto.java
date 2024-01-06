package com.maxzamota.spring_sandbox.dto;

import com.maxzamota.spring_sandbox.enums.Gender;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDto {
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private Integer age;
    @NonNull
    private Gender gender;

}
