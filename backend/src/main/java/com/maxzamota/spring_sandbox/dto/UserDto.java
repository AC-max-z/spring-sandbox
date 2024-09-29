package com.maxzamota.spring_sandbox.dto;

import com.maxzamota.spring_sandbox.enums.UserRole;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private Integer id;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private UserRole role;
    private Boolean isActive;
}
