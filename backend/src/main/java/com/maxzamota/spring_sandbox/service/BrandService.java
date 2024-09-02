package com.maxzamota.spring_sandbox.service;

import com.maxzamota.spring_sandbox.exception.BadRequestException;
import com.maxzamota.spring_sandbox.exception.DuplicateResourceException;
import com.maxzamota.spring_sandbox.exception.ResourceNotFoundException;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Service
public class BrandService {
    private final BrandRepository repository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.repository = brandRepository;
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
        brand.setDateAdded(Objects.nonNull(currentBrand.getDateAdded())
                ? currentBrand.getDateAdded()
                : new Timestamp(System.currentTimeMillis()));
        return this.repository.save(brand);
    }

    public BrandEntity findBrandByName(String name) {
        return this.repository.findOneByName(name);
    }

    public Collection<BrandEntity> saveAll(Collection<BrandEntity> brands) {
        return this.repository.saveAll(brands);
    }

    public Page<BrandEntity> getAll(
            Integer pageNum,
            Integer pageSize,
            String sortBy,
            String sortDirection
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        return this.repository.findAll(pageable);
    }

    public Page<BrandEntity> getAll(Pageable pageable) {
        Page<BrandEntity> brands;
        try {
            brands = this.repository.findAll(pageable);
            return brands;
        } catch (PropertyReferenceException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
