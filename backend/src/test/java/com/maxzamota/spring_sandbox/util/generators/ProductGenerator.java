package com.maxzamota.spring_sandbox.util.generators;

import com.github.javafaker.Faker;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.model.ProductEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ProductGenerator {

    private final ProductEntity product;
    private final List<ProductEntity> products = new ArrayList<>();

    public ProductGenerator() {
        var faker = new Faker();
        var name = faker.beer().name();
        var description = faker.lorem().sentence(100);
        var price = ThreadLocalRandom.current().nextDouble(10.00, Double.MAX_VALUE);
        var issueDate = new Timestamp(
                faker
                        .date()
                        .past(10000, TimeUnit.DAYS)
                        .getTime()
        );
        var brand = new BrandGenerator().generate();
        var availableAmt = ThreadLocalRandom.current().nextInt(0, 9999);
        var discount = ThreadLocalRandom.current().nextInt(0, 99);
        this.product = new ProductEntity(
                name,
                description,
                price,
                issueDate,
                brand,
                availableAmt,
                discount
        );
    }

    public ProductGenerator withId(Integer id) {
        this.product.setId(id);
        return this;
    }

    public ProductGenerator withName(String name) {
        this.product.setName(name);
        return this;
    }

    public ProductGenerator withDescription(String description) {
        this.product.setDescription(description);
        return this;
    }

    public ProductGenerator withPrice(Double price) {
        this.product.setPrice(price);
        return this;
    }

    public ProductGenerator withIssueDate(Timestamp date) {
        this.product.setIssueDate(date);
        return this;
    }

    public ProductGenerator withBrand(BrandEntity brand) {
        this.product.setBrand(brand);
        return this;
    }

    public ProductGenerator withAmount(Integer amount) {
        this.product.setAvailableAmount(amount);
        return this;
    }

    public ProductGenerator withDiscount(Integer discount) {
        this.product.setDiscount(discount);
        return this;
    }

    public ProductEntity generate() {
        return this.product;
    }

    public Collection<ProductEntity> buildList(int size) {
        var faker = new Faker();
        for (int i = 0; i < size; i++) {
            this.products.add(
                    new ProductEntity(
                            faker.beer().name(),
                            faker.lorem().sentence(100),
                            ThreadLocalRandom.current().nextDouble(10.00, Double.MAX_VALUE),
                            new Timestamp(faker.date().past(10000, TimeUnit.DAYS).getTime()),
                            new BrandGenerator().generate(),
                            ThreadLocalRandom.current().nextInt(0, 9999),
                            ThreadLocalRandom.current().nextInt(0, 99)
                    )
            );
        }
        return this.products;
    }
}
