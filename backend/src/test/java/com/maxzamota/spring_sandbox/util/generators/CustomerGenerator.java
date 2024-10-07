package com.maxzamota.spring_sandbox.util.generators;

import com.github.javafaker.Faker;
import com.maxzamota.spring_sandbox.enums.Gender;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.model.CustomerEntity;
import com.maxzamota.spring_sandbox.util.FakerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerGenerator implements ObjectGenerator<CustomerEntity> {
    private static final Faker FAKER = FakerProvider.getInstance();
    private static final CustomerEntity.CustomerBuilder BUILDER = new CustomerEntity.CustomerBuilder();

    public CustomerGenerator() {
        buildNew();
    }

    public CustomerGenerator buildNew() {
        BUILDER.setId(null);
        BUILDER.setName(FAKER.name().name());
        BUILDER.setEmail(FAKER.internet().safeEmailAddress());
        BUILDER.setAge(FAKER.number().numberBetween(16, 99));
        BUILDER.setGender(Gender.values()[new Random().nextInt(Gender.values().length)]);
        BUILDER.setDeleted(false);
        return this;
    }

    public CustomerGenerator withId(int id) {
        BUILDER.setId(id);
        return this;
    }

    public CustomerGenerator withName(String name) {
        BUILDER.setName(name);
        return this;
    }

    public CustomerGenerator withEmail(String email) {
        BUILDER.setEmail(email);
        return this;
    }

    public CustomerGenerator withAge(int age) {
        BUILDER.setAge(age);
        return this;
    }

    public CustomerGenerator withGender(Gender gender) {
        BUILDER.setGender(gender);
        return this;
    }

    public CustomerGenerator withIsDeleted(boolean isDeleted) {
        BUILDER.setDeleted(isDeleted);
        return this;
    }

    @Override
    public CustomerEntity generate() {
        return BUILDER.build();
    }

    @Override
    public List<CustomerEntity> generateMany(int howMany) {
        if (howMany <= 0) {
            return List.of();
        }
        var list = new ArrayList<CustomerEntity>(howMany);
        for (int i = 0; i < howMany; i++) {
            list.add(this.buildNew().generate());
        }
        return list.stream().toList();
    }
}
