package com.maxzamota.spring_sandbox.util.generators;

import com.github.javafaker.Faker;
import com.maxzamota.spring_sandbox.model.BrandEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BrandGenerator {
    private final BrandEntity brand;
    private final List<BrandEntity> brands = new ArrayList<>();

    public BrandGenerator() {
        Faker faker = new Faker();
        String name = faker.company().name();
        Timestamp foundationDate = new Timestamp(
                faker.date().past(10000, TimeUnit.DAYS).getTime());
        String countryOfOrigin = faker.address().country();
        String description = faker.chuckNorris().fact();
        String history = faker.lorem().sentence(42);
//        Timestamp dateAdded = new Timestamp(System.currentTimeMillis());
        this.brand = new BrandEntity(
                name,
                foundationDate,
                countryOfOrigin,
                description,
                history,
                false
        );
    }

    public BrandGenerator withId(Integer id) {
        this.brand.setId(id);
        return this;
    }

    public BrandGenerator withName(String name) {
        this.brand.setName(name);
        return this;
    }

    public BrandGenerator withFoundationDate(Timestamp date) {
        this.brand.setFoundationDate(date);
        return this;
    }

    public BrandGenerator withDescription(String description) {
        this.brand.setDescription(description);
        return this;
    }

    public BrandGenerator withCountry(String country) {
        this.brand.setCountryOfOrigin(country);
        return this;
    }

    public BrandGenerator withHistory(String history) {
        this.brand.setHistory(history);
        return this;
    }

    public BrandGenerator withDateAdded(Timestamp dateAdded) {
        this.brand.setDateAdded(dateAdded);
        return this;
    }

    public BrandEntity generate() {
        return this.brand;
    }

    public Collection<BrandEntity> buildList(int size) {
        Faker faker = new Faker();
        for (int i = 0; i < size; i++) {
            brands.add(
                    new BrandEntity(
                            faker.company().name(),
                            new Timestamp(faker.date().past(10000, TimeUnit.DAYS).getTime()),
                            faker.address().country(),
                            faker.gameOfThrones().quote(),
                            faker.lorem().sentence(42),
                            false
                    )
            );
        }
        return brands;
    }
}
