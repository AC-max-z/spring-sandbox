package com.maxzamota.springbootexample.generators;

import com.github.javafaker.Faker;
import com.maxzamota.springbootexample.model.Customer;

import java.util.UUID;

public class CustomerGenerator {

    private final Customer customer;

    public CustomerGenerator() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "-" + UUID.randomUUID() + "@" + faker.internet().domainName();
        this.customer = new Customer(
                firstName + " " + lastName,
                email,
                faker.number().numberBetween(0, 99)
        );
    }

    public CustomerGenerator withName(String name) {
        this.customer.setName(name);
        return this;
    }

    public CustomerGenerator withEmail(String email) {
        this.customer.setEmail(email);
        return this;
    }

    public CustomerGenerator withAge(int age) {
        this.customer.setAge(age);
        return this;
    }

    public CustomerGenerator withId(int id) {
        this.customer.setId(id);
        return this;
    }

    public Customer build() {
        return this.customer;
    }
}
