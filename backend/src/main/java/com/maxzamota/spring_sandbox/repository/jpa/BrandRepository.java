package com.maxzamota.spring_sandbox.repository.jpa;

import com.maxzamota.spring_sandbox.model.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
}
