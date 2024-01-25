package com.maxzamota.spring_sandbox.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private Double price;
    @NonNull
    private Timestamp issueDate;
    @NonNull
    private BrandDto brand;
    @NonNull
    private Integer availableAmount;
    private Integer discount;
}
