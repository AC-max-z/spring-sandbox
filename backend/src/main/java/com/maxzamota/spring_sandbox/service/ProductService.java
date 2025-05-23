package com.maxzamota.spring_sandbox.service;

import com.maxzamota.spring_sandbox.exception.BadRequestException;
import com.maxzamota.spring_sandbox.exception.DuplicateResourceException;
import com.maxzamota.spring_sandbox.exception.ResourceNotFoundException;
import com.maxzamota.spring_sandbox.model.ProductEntity;
import com.maxzamota.spring_sandbox.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@Slf4j
public class ProductService implements EntityService<Integer, ProductEntity> {
    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public ProductEntity getById(Integer id) {
        String username = getUsername();
        log.info("Attempt to fetch Product by id {} by user {}",
                id, keyValue("username", username));
        return this.repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Fetch Product by id {} by user {} failed - not found",
                            id, keyValue("username", username));
                    return new ResourceNotFoundException("Entity with id={%s} not found"
                            .formatted(id));
                });
    }

    @Override
    public ProductEntity save(ProductEntity product) {
        String username = getUsername();
        log.info("Attempt to save Product {} by user {}", product, keyValue("username", username));
        if (this.repository.existsByName(product.getName())) {
            log.warn("Attempt to save Product {} with duplicate name by user {}",
                    product, keyValue("username", username));
            throw new DuplicateResourceException("Entity with name={%s} already exists"
                    .formatted(product.getName()));
        }
        ProductEntity savedProduct = this.repository.save(product);
        log.info("Product {} successfully saved by user {}", savedProduct,
                keyValue("username", username));
        return savedProduct;
    }

    @Override
    public String deleteById(Integer id) {
        String username = getUsername();
        log.info("Attempt to soft-delete Product by id {} by user {}",
                id, keyValue("username", username));
        this.repository.deleteById(id);
        return "Entity with id={%s} successfully deleted (or ignored if it did not exist in the first place)!"
                .formatted(id);
    }

    @Override
    public ProductEntity update(ProductEntity product) {
        String username = getUsername();
        log.info("Attempt to update Product {} by user {}",
                product, keyValue("username", username));

        if (!this.repository.existsById(product.getId())) {
            log.warn("Product with id {} not found, update by user {} is aborted",
                    product.getId(), keyValue("username", username));
            throw new ResourceNotFoundException("Product with id={%s} not found!"
                    .formatted(product.getId()));
        }

        ProductEntity currentProduct = this.repository.findById(product.getId()).orElse(null);
        if (product.equals(currentProduct)) {
            log.info("Product {} update by user {} aborted because it is identical to currently stored",
                    product, keyValue("username", username));
            return product;
        }

        if (!this.repository.findAllByName(product.getName())
                .stream()
                .filter(b -> !Objects.equals(b.getId(), product.getId()))
                .toList()
                .isEmpty()
        ) {
            log.warn("Product {} update by user {} failed: product with name {} already exists",
                    product, keyValue("username", username), product.getName());
            throw new DuplicateResourceException("Product with name={%s} already exists"
                    .formatted(product.getName()));
        }

        product.setDateAdded(Objects.nonNull(currentProduct.getDateAdded())
                ? currentProduct.getDateAdded()
                : new Timestamp(System.currentTimeMillis())
        );
        ProductEntity updated = this.repository.save(product);
        log.info("Product {} successfully updated by user {}",
                updated, keyValue("username", username));
        return updated;
    }

    public Collection<ProductEntity> saveAll(Collection<ProductEntity> products) {
        return this.repository.saveAll(products);
    }

    @Override
    public Page<ProductEntity> getAll(Pageable pageable) {
        String username = getUsername();
        log.info("Attempt to fetch all Products with pageable {} by user {}",
                pageable, keyValue("username", username));
        try {
            Page<ProductEntity> products = this.repository.findAll(pageable);
            log.info("Products successfully fetched by user {}",
                    keyValue("username", username));
            return products;
        } catch (PropertyReferenceException e) {
            log.error("Exception fetching Products by user {}", keyValue("username", username));
            log.error("Message: {}", e.getMessage());
            log.debug("Stacktrace: {}", Arrays.toString(e.getStackTrace()));
            throw new BadRequestException(e.getMessage());
        }
    }
}
