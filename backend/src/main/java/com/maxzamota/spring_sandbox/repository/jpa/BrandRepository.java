package com.maxzamota.spring_sandbox.repository.jpa;

import com.maxzamota.spring_sandbox.model.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
    Collection<BrandEntity> findAllByOrderByIdAsc();
    Collection<BrandEntity> findAllByOrderByIdDesc();
    Collection<BrandEntity> findAllByOrderByNameAsc();
    Collection<BrandEntity> findAllByOrderByNameDesc();
    boolean existsBrandByName(String name);
    boolean existsBrandById(Integer id);
    Collection<BrandEntity> findAllByName(String name);
    BrandEntity findOneByName(String name);
}
