package com.maxzamota.spring_sandbox.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Table(
        name = "product",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "product_name_unique",
                        columnNames = "name"
                )
        }
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
    private Integer id;

    @Column(
            name = "name",
            nullable = false,
            unique = true
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
    private Timestamp issueDate;

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
    private Integer discount;

    @Column(
            name = "date_added",
            nullable = false
    )
    @NonNull
    private Timestamp dateAdded;

    @Column(
            name = "is_deleted",
            nullable = false
    )
    private boolean isDeleted;

    public ProductEntity(
            @NonNull String name,
            @NonNull String description,
            @NonNull Double price,
            @NonNull Timestamp issueDate,
            @NonNull BrandEntity brand,
            @NonNull Integer availableAmount,
            Integer discount,
            boolean isDeleted
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.issueDate = issueDate;
        this.brand = brand;
        this.availableAmount = availableAmount;
        this.discount = discount;
        this.isDeleted = isDeleted;
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
        this.isDeleted = builder.isDeleted;
    }

    @Setter
    public static class ProductBuilder {
        private Integer id;
        private String name;
        private String description;
        private Double price;
        private Timestamp issueDate;
        private BrandEntity brand;
        private Integer availableAmount;
        private Integer discount;
        private Timestamp dateAdded;
        private boolean isDeleted;

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
                && Objects.equals(dateAdded, that.dateAdded)
                && isDeleted == that.isDeleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                name,
                description,
                price,
                issueDate,
                brand,
                availableAmount,
                discount,
                dateAdded,
                isDeleted
        );
    }
}
