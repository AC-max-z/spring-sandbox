package com.maxzamota.spring_sandbox.repository;

import com.maxzamota.spring_sandbox.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    boolean existsByName(String name);
    boolean existsById(Integer id);
    Collection<ProductEntity> findAllByName(String name);
}
