package com.maxzamota.spring_sandbox.service;

import com.maxzamota.spring_sandbox.enums.ProductSortType;
import com.maxzamota.spring_sandbox.exception.DuplicateResourceException;
import com.maxzamota.spring_sandbox.exception.ResourceNotFoundException;
import com.maxzamota.spring_sandbox.model.ProductEntity;
import com.maxzamota.spring_sandbox.repository.jpa.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    public Collection<ProductEntity> getAllSorted(ProductSortType sort) {
        return switch (sort) {
            case BY_ID_DESC -> this.repository.findAllByOrderByIdDesc();
            case BY_ID_ASC -> this.repository.findAllByOrderByIdAsc();
            case BY_NAME_ASC -> this.repository.findAllByOrderByNameAsc();
            case BY_NAME_DESC -> this.repository.findAllByOrderByNameDesc();
        };
    }

    public ProductEntity getById(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity with id={%s} not found"
                        .formatted(id)));
    }

    public ProductEntity save(ProductEntity product) {
        if (this.repository.existsBrandByName(product.getName())) {
            throw new DuplicateResourceException("Entity with name={%s} already exists"
                    .formatted(product.getName()));
        }
        return this.repository.save(product);
    }

    public String deleteById(Integer id) {
        this.repository.deleteById(id);
        return "Entity with id={%s} successfully deleted (or ignored if it did not exist in the first place)!"
                .formatted(id);

    }

    public ProductEntity update(ProductEntity product) {
        if (!this.repository.existsBrandById(product.getId())) {
            throw new ResourceNotFoundException("Product with id={%s} not found!"
                    .formatted(product.getId()));
        }
        ProductEntity currentProduct = this.repository.findById(product.getId()).orElseGet(() -> null);
        if (product.equals(currentProduct)) {
            return product;
        }
        if (!this.repository.findAllByName(product.getName())
                .stream()
                .filter(b -> !Objects.equals(b.getId(), product.getId()))
                .toList()
                .isEmpty()
        ) {
            throw new DuplicateResourceException("Brand with name={%s} already exists"
                    .formatted(product.getName()));
        }
        product.setDateAdded(Objects.nonNull(currentProduct.getDateAdded())
                ? currentProduct.getDateAdded()
                : new Timestamp(System.currentTimeMillis()));
        return this.repository.save(product);
    }

    public Collection<ProductEntity> saveAll(Collection<ProductEntity> products) {
        return this.repository.saveAll(products);
    }

    public Collection<ProductEntity> getAll() {
        return this.repository.findAll();
    }
}
