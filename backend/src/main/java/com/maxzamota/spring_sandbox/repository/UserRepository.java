package com.maxzamota.spring_sandbox.repository;

import com.maxzamota.spring_sandbox.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Collection<UserEntity> findAllByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
}
