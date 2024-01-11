package com.maxzamota.spring_sandbox.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BrandDto {
    @NonNull
    private String name;
    @NonNull
    private Date foundationDate;
    @NonNull
    private String countryOfOrigin;
    @NonNull
    private String description;
    @NonNull
    private String history;
}
