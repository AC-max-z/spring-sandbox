package com.maxzamota.spring_sandbox.repository.jpa;

import com.maxzamota.spring_sandbox.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    Collection<ProductEntity> findAllByOrderByIdAsc();
    Collection<ProductEntity> findAllByOrderByIdDesc();
    Collection<ProductEntity> findAllByOrderByNameAsc();
    Collection<ProductEntity> findAllByOrderByNameDesc();
    boolean existsBrandByName(String name);
    boolean existsBrandById(Integer id);
    Collection<ProductEntity> findAllByName(String name);
}
