package com.maxzamota.spring_sandbox.util.generators;

import com.github.javafaker.Faker;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.util.FakerProvider;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BrandGenerator implements ObjectGenerator<BrandEntity> {
    private static final Faker FAKER = FakerProvider.getInstance();
    private static final BrandEntity.BrandBuilder BUILDER = new BrandEntity.BrandBuilder();

    public BrandGenerator() {
        buildNew();
    }

    public BrandGenerator buildNew() {
        BUILDER.setId(null);
        BUILDER.setName(FAKER.company().name());
        BUILDER.setDescription(FAKER.lorem().characters(42));
        BUILDER.setHistory(FAKER.lorem().characters(69));
        BUILDER.setCountryOfOrigin(FAKER.address().country());
        BUILDER.setDateAdded(new Timestamp(System.currentTimeMillis()));
        BUILDER.setFoundationDate(new Timestamp(System.currentTimeMillis()));
        BUILDER.setDeleted(false);
        return this;
    }

    public BrandGenerator withId(int id) {
        BUILDER.setId(id);
        return this;
    }

    public BrandGenerator withName(String name) {
        BUILDER.setName(name);
        return this;
    }

    public BrandGenerator withDescription(String description) {
        BUILDER.setDescription(description);
        return this;
    }

    public BrandGenerator withHistory(String history) {
        BUILDER.setHistory(history);
        return this;
    }

    public BrandGenerator withDateAdded(Timestamp dateAdded) {
        BUILDER.setDateAdded(dateAdded);
        return this;
    }

    public BrandGenerator withFoundationDate(Timestamp foundationDate) {
        BUILDER.setFoundationDate(foundationDate);
        return this;
    }

    public BrandGenerator withCountryOfOrigin(String country) {
        BUILDER.setCountryOfOrigin(country);
        return this;
    }

    public BrandGenerator withIsDeleted(boolean isDeleted) {
        BUILDER.setDeleted(isDeleted);
        return this;
    }

    @Override
    public BrandEntity generate() {
        return BUILDER.build();
    }

    @Override
    public List<BrandEntity> generateMany(int howMany) {
        if (howMany <= 0) {
            return List.of();
        }
        var list = new ArrayList<BrandEntity>(howMany);
        for (int i = 0; i < howMany; i++) {
            list.add(this.buildNew().generate());
        }
        return list.stream().toList();
    }
}
