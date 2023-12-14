package com.maxzamota.springbootexample.dto;

public record CustomerDto(
        String name,
        String email,
        Integer age
) {
}
