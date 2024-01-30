package com.maxzamota.spring_sandbox.service;

import com.maxzamota.spring_sandbox.enums.BrandSortType;
import com.maxzamota.spring_sandbox.exception.DuplicateResourceException;
import com.maxzamota.spring_sandbox.exception.ResourceNotFoundException;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.repository.jpa.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Service
public class BrandService {
    private final BrandRepository repository;

    @Autowired
    public BrandService(BrandRepository repository) {
        this.repository = repository;
    }

    public Collection<BrandEntity> getSortedBrands(BrandSortType sortType) {
        return switch (sortType) {
            case BY_ID_ASC -> this.repository.findAllByOrderByIdAsc();
            case BY_ID_DESC -> this.repository.findAllByOrderByIdDesc();
            case BY_NAME_ASC -> this.repository.findAllByOrderByNameAsc();
            case BY_NAME_DESC -> this.repository.findAllByOrderByNameDesc();
        };
    }

    public BrandEntity getBrandById(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id={%s} not found!".formatted(id)));
    }

    public BrandEntity save(BrandEntity brand) {
        if (this.repository.existsBrandByName(brand.getName())) {
            throw new DuplicateResourceException("Brand with name={%s} already exists!".formatted(brand.getName()));
        }
        return this.repository.save(brand);
    }

    public String deleteById(Integer id) {
        this.repository.deleteById(id);
        return "Entity with id={%s} successfully deleted (or ignored if it did not exist in the first place)!"
                .formatted(id);
    }

    public BrandEntity update(BrandEntity brand) {
        if (!this.repository.existsBrandById(brand.getId())) {
            throw new ResourceNotFoundException("Brand with id={%s} not found!".formatted(brand.getId()));
        }
        BrandEntity currentBrand = this.repository.findById(brand.getId()).orElseGet(() -> null);
        if (brand.equals(currentBrand)) {
            return brand;
        }
        if (!this.repository.findAllByName(brand.getName())
                .stream()
                .filter(b -> !Objects.equals(b.getId(), brand.getId()))
                .toList()
                .isEmpty()
        ) {
            throw new DuplicateResourceException("Brand with name={%s} already exists".formatted(brand.getName()));
        }
        brand.setDateAdded(Objects.nonNull(currentBrand.getDateAdded()) ? currentBrand.getDateAdded() : new Timestamp(System.currentTimeMillis()));
        return this.repository.save(brand);
    }

    public BrandEntity findBrandByName(String name) {
        return this.repository.findOneByName(name);
    }

    public Collection<BrandEntity> saveAll(Collection<BrandEntity> brands) {
        return this.repository.saveAll(brands);
    }

    public Collection<BrandEntity> getAll() {
        return this.repository.findAll();
    }
}
