package com.maxzamota.spring_sandbox.dto;

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
    private String role;
    private Boolean isActive;
}
