package com.maxzamota.spring_sandbox.repository;

import com.maxzamota.spring_sandbox.model.CustomerEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository("jpa")
@Primary
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    boolean existsByEmail(String email);
    boolean existsById(Integer id);
    Collection<CustomerEntity> findAllByEmail(String email);
}
