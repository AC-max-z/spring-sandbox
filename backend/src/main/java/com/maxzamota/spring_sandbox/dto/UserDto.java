package com.maxzamota.spring_sandbox.dto;

import com.maxzamota.spring_sandbox.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private Integer id;
    @Email
    @NonNull
    private String email;
    @Size(min = 3, message = "Password must be at least 3 characters long")
    @NonNull
    private String password;
    @NonNull
    private UserRole role;
    private Boolean isActive;
}
