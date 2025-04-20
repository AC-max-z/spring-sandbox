package com.maxzamota.spring_sandbox.dto;

import com.maxzamota.spring_sandbox.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDto {
    private Integer id;
    @NonNull
    private String name;
    @Email
    @NonNull
    private String email;
    @Min(16)
    @Max(99)
    @NonNull
    private Integer age;
    @NonNull
    private Gender gender;

}
