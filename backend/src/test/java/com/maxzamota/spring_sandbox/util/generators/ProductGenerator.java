package com.maxzamota.spring_sandbox.util.generators;

import com.github.javafaker.Faker;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.model.ProductEntity;
import com.maxzamota.spring_sandbox.util.FakerProvider;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class ProductGenerator implements ObjectGenerator<ProductEntity> {
    private final ProductEntity.ProductBuilder BUILDER = new ProductEntity.ProductBuilder();

    public ProductGenerator() {
        buildNew();
    }

    public ProductGenerator buildNew() {
        Faker FAKER = FakerProvider.getInstance();
        BUILDER.setId(null);
        BUILDER.setName(FAKER.company().name());
        BUILDER.setDescription(FAKER.lorem().characters(42));
        BUILDER.setPrice(420.69);
        BUILDER.setIssueDate(new Timestamp(
                FAKER.date().past(40 * 365, TimeUnit.DAYS).getTime()
        ));
        BUILDER.setBrand(new BrandGenerator().generate());
        BUILDER.setAvailableAmount(42);
        BUILDER.setDiscount(15);
        BUILDER.setDateAdded(new Timestamp(System.currentTimeMillis()));
        BUILDER.setDeleted(false);
        return this;
    }

    public ProductGenerator withId(int id) {
        BUILDER.setId(id);
        return this;
    }

    public ProductGenerator withName(String name) {
        BUILDER.setName(name);
        return this;
    }

    public ProductGenerator withDescription(String description) {
        BUILDER.setDescription(description);
        return this;
    }

    public ProductGenerator withPrice(double price) {
        BUILDER.setPrice(price);
        return this;
    }

    public ProductGenerator withIssueDate(Timestamp issueDate) {
        BUILDER.setIssueDate(issueDate);
        return this;
    }

    public ProductGenerator withBrand(BrandEntity brand) {
        BUILDER.setBrand(brand);
        return this;
    }

    public ProductGenerator withAvailableAmount(int amount) {
        BUILDER.setAvailableAmount(amount);
        return this;
    }

    public ProductGenerator withDiscount(int discount) {
        BUILDER.setDiscount(discount);
        return this;
    }

    public ProductGenerator withDateAdded(Timestamp dateAdded) {
        BUILDER.setDateAdded(dateAdded);
        return this;
    }

    public ProductGenerator withIsDeleted(boolean isDeleted) {
        BUILDER.setDeleted(isDeleted);
        return this;
    }

    @Override
    public ProductEntity generate() {
        return BUILDER.build();
    }

}
