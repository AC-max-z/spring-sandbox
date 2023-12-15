package com.maxzamota.springbootexample.generators;

import com.github.javafaker.Faker;
import com.maxzamota.springbootexample.model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CustomerGenerator {

    private final Customer customer;
    private final List<Customer> customers = new ArrayList<>();

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

    public Collection<Customer> buildList(int size) {
        Faker faker = new Faker();
        for (int i = 0; i < size; i++) {
            customers.add(
                    new Customer(
                            faker.number().numberBetween(0, 1000),
                            faker.name().fullName(),
                            faker.internet().safeEmailAddress(),
                            faker.number().numberBetween(0, 99)
                    )
            );
        }
        return customers;
    }
}
