package com.maxzamota.spring_sandbox.repository.jpa;

import com.maxzamota.spring_sandbox.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
}
