package com.maxzamota.spring_sandbox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maxzamota.spring_sandbox.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE users SET is_active = false WHERE id=?")
@SQLRestriction("is_active <> FALSE")
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
    @JsonIgnore
    @NonNull
    private String password;

    @Column(nullable = false, name = "role")
    @ToString.Include
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::user_role")
    @NonNull
    private UserRole role;

    @Column(nullable = false, name = "is_active")
    @JsonIgnore
    @ToString.Include
    private Boolean isActive = Boolean.TRUE;

    public UserEntity(
            @NonNull String email,
            @NonNull String password,
            @NonNull UserRole role,
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
        return Objects.equals(id, that.id)
                && Objects.equals(email, that.email)
                && Objects.equals(password, that.password)
                && Objects.equals(role, that.role)
                && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, role, isActive);
    }
}
