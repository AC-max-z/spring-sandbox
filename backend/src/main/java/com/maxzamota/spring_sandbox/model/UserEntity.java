package com.maxzamota.spring_sandbox.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_email_unique",
                        columnNames = "email"
                )
        }
)
public class UserEntity {
    @Id
    @Column(nullable = false, name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer id;

    @Column(nullable = false, name = "email", unique = true)
    @ToString.Include
    @NonNull
    private String email;

    @Column(nullable = false, name = "password")
    @ToString.Include
    @NonNull
    private String password;

    @Column(nullable = false, name = "role")
    @ToString.Include
    @NonNull
    private String role;

    @Column(nullable = false, name = "is_active")
    @ToString.Include
    private Boolean isActive;

    public UserEntity(
            @NonNull String email,
            @NonNull String password,
            @NonNull String role,
            @NonNull Boolean isActive
    ) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(role, that.role) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, role, isActive);
    }
}
