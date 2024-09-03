package com.maxzamota.spring_sandbox.repository;

import com.maxzamota.spring_sandbox.model.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
    boolean existsByName(String name);
    boolean existsById(Integer id);
    Collection<BrandEntity> findAllByName(String name);
    BrandEntity findOneByName(String name);
}
