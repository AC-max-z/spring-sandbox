package com.maxzamota.spring_sandbox.dto;

import lombok.*;

import java.util.Date;

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
    private Date issueDate;
    @NonNull
    private BrandDto brand;
    @NonNull
    private Integer availableAmount;
    private Integer discount;
}
