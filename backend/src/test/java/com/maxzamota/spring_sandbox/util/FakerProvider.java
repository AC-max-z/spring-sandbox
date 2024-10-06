package com.maxzamota.spring_sandbox.util;

import com.github.javafaker.Faker;

public class FakerProvider {
    private static final Faker FAKER = new Faker();

    public static Faker getInstance() {
        return FAKER;
    }
}