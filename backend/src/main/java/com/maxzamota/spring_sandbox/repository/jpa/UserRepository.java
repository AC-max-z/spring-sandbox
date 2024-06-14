package com.maxzamota.spring_sandbox.repository.jpa;

import com.maxzamota.spring_sandbox.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Collection<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
}
