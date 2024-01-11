package com.maxzamota.spring_sandbox.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Table(
        name = "product",
        uniqueConstraints = {}
)
public class ProductEntity {
    @Id
    @Column(
            name = "id",
            nullable = false,
            unique = true
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private UUID id;

    @Column(
            name = "name",
            nullable = false
    )
    @ToString.Include
    @NonNull
    private String name;

    @Column(
            name = "description",
            nullable = false
    )
    @ToString.Include
    @NonNull
    private String description;

    @Column(
            name = "price",
            nullable = false
    )
    @ToString.Include
    @NonNull
    private Double price;

    @Column(
            name = "issue_date",
            nullable = false
    )
    @ToString.Include
    @NonNull
    private Date issueDate;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    @ToString.Include
    @NonNull
    private BrandEntity brand;

    @Column(
            name = "available_amount",
            nullable = false
    )
    @ToString.Include
    @NonNull
    private Integer availableAmount;

    @Column(
            name = "discount",
            nullable = true
    )
    @ToString.Exclude
    private Integer discount;

    @Column(
            name = "date_added",
            nullable = false
    )
    @ToString.Exclude
    @NonNull
    private Date dateAdded;

    public ProductEntity(
            @NonNull String name,
            @NonNull String description,
            @NonNull Double price,
            @NonNull Date issueDate,
            @NonNull BrandEntity brand,
            @NonNull Integer availableAmount,
            Integer discount
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.issueDate = issueDate;
        this.brand = brand;
        this.availableAmount = availableAmount;
        this.discount = discount;
    }

    public ProductEntity(ProductBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.issueDate = builder.issueDate;
        this.brand = builder.brand;
        this.availableAmount = builder.availableAmount;
        this.discount = builder.discount;
        this.dateAdded = builder.dateAdded;
    }

    @Setter
    public static class ProductBuilder {
        private UUID id;
        private String name;
        private String description;
        private Double price;
        private Date issueDate;
        private BrandEntity brand;
        private Integer availableAmount;
        private Integer discount;
        private Date dateAdded;

        public ProductEntity build() {
            return new ProductEntity(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(price, that.price)
                && Objects.equals(issueDate, that.issueDate)
                && Objects.equals(brand, that.brand)
                && Objects.equals(availableAmount, that.availableAmount)
                && Objects.equals(discount, that.discount)
                && Objects.equals(dateAdded, that.dateAdded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, issueDate, brand, availableAmount, discount, dateAdded);
    }
}
