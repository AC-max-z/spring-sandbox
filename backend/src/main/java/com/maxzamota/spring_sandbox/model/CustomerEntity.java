package com.maxzamota.spring_sandbox.model;

import com.maxzamota.spring_sandbox.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "customer_email_unique",
                        columnNames = "email"
                )
        }
)
public class CustomerEntity {
    @Id
    @Column(nullable = false, name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer id;

    @Column(nullable = false)
    @ToString.Include
    @NonNull
    private String name;

    @Column(nullable = false, unique = true)
    @ToString.Include
    @NonNull
    private String email;

    @Column(nullable = false)
    @ToString.Include
    @NonNull
    private Integer age;

    @Column(nullable = false)
    @ToString.Include
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::gender")
    @NonNull
    private Gender gender;

    public CustomerEntity(String name, String email, Integer age, Gender gender) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    public CustomerEntity(CustomerBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
        this.gender = builder.gender;
    }

    @Setter
    public static class CustomerBuilder {
        private Integer id;
        private String name;
        private String email;
        private Integer age;
        private Gender gender;

        public CustomerEntity build() {
            return new CustomerEntity(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity that = (CustomerEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(age, that.age) && gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age, gender);
    }
}
