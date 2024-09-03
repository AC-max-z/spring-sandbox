package com.maxzamota.spring_sandbox.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE brand SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted <> TRUE")
@ToString(onlyExplicitlyIncluded = true)
@Table(
        name = "brand",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "brand_name_unique",
                        columnNames = "name"
                )
        }
)
public class BrandEntity {
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
            name = "foundation_date",
            nullable = false
    )
    @ToString.Include
    @NonNull
    private Timestamp foundationDate;

    @Column(
            name = "country_of_origin",
            nullable = false
    )
    @ToString.Include
    @NonNull
    private String countryOfOrigin;

    @Column(
            name = "description",
            nullable = false
    )
    @ToString.Include
    @NonNull
    private String description;

    @Column(
            name = "history",
            nullable = false
    )
    @ToString.Include
    @NonNull
    private String history;

    @Column(
            name = "date_added",
            nullable = false
    )
    private Timestamp dateAdded;

    @Column(
            name = "is_deleted",
            nullable = false
    )
    private boolean isDeleted = Boolean.FALSE;

    public BrandEntity(BrandBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.foundationDate = builder.foundationDate;
        this.countryOfOrigin = builder.countryOfOrigin;
        this.description = builder.description;
        this.history = builder.history;
        this.dateAdded = builder.dateAdded;
        this.isDeleted = builder.isDeleted;
    }

    public BrandEntity(
            @NonNull String name,
            @NonNull Timestamp foundationDate,
            @NonNull String countryOfOrigin,
            @NonNull String description,
            @NonNull String history,
            boolean isDeleted
    ) {
        this.name = name;
        this.foundationDate = foundationDate;
        this.countryOfOrigin = countryOfOrigin;
        this.description = description;
        this.history = history;
        this.isDeleted = isDeleted;
    }

    @Setter
    public static class BrandBuilder {
        private Integer id;
        private String name;
        private Timestamp foundationDate;
        private String countryOfOrigin;
        private String description;
        private String history;
        private Timestamp dateAdded;
        private boolean isDeleted;

        public BrandEntity build() {
            return new BrandEntity(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandEntity that = (BrandEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(foundationDate, that.foundationDate)
                && Objects.equals(countryOfOrigin, that.countryOfOrigin)
                && Objects.equals(description, that.description)
                && Objects.equals(history, that.history)
                && Objects.equals(dateAdded, that.dateAdded)
                && Objects.equals(isDeleted, that.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                name,
                foundationDate,
                countryOfOrigin,
                description,
                history,
                dateAdded,
                isDeleted
        );
    }
}
