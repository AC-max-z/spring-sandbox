package com.maxzamota.spring_sandbox.util.generators;

import com.github.javafaker.Faker;
import com.maxzamota.spring_sandbox.enums.Gender;
import com.maxzamota.spring_sandbox.model.CustomerEntity;

import java.util.*;

public class CustomerGenerator {

    private final CustomerEntity customerEntity;
    private final List<CustomerEntity> customerEntities = new ArrayList<>();

    public CustomerGenerator() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "-"
                + UUID.randomUUID() + "@" + faker.internet().domainName();
        this.customerEntity = new CustomerEntity(
                firstName + " " + lastName,
                email,
                faker.number().numberBetween(0, 99),
                Gender.values()[new Random().nextInt(Gender.values().length)]
        );
    }

    public CustomerGenerator withName(String name) {
        this.customerEntity.setName(name);
        return this;
    }

    public CustomerGenerator withEmail(String email) {
        this.customerEntity.setEmail(email);
        return this;
    }

    public CustomerGenerator withAge(int age) {
        this.customerEntity.setAge(age);
        return this;
    }

    public CustomerGenerator withId(int id) {
        this.customerEntity.setId(id);
        return this;
    }

    public CustomerGenerator withGender(Gender gender) {
        this.customerEntity.setGender(gender);
        return this;
    }

    public CustomerEntity generate() {
        return this.customerEntity;
    }

    public Collection<CustomerEntity> buildList(int size) {
        Faker faker = new Faker();
        for (int i = 0; i < size; i++) {
            customerEntities.add(
                    new CustomerEntity(
                            faker.number().numberBetween(0, 1000),
                            faker.name().fullName(),
                            faker.internet().safeEmailAddress(),
                            faker.number().numberBetween(0, 99),
                            Gender.values()[new Random().nextInt(Gender.values().length)]
                    )
            );
        }
        return customerEntities;
    }
}
