package com.maxzamota.spring_sandbox.service;

import com.maxzamota.spring_sandbox.exception.BadRequestException;
import com.maxzamota.spring_sandbox.exception.DuplicateResourceException;
import com.maxzamota.spring_sandbox.exception.ResourceNotFoundException;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.repository.BrandRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@Slf4j
public class BrandService {
    private final BrandRepository repository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.repository = brandRepository;
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Objects.nonNull(authentication.getName()) ? authentication.getName() : "anonymous";
    }

    public BrandEntity getBrandByIdOrThrow(Integer id) {
        String username = getUsername();
        log.info("Attempt to fetch Brand by id {} by user {}", id, keyValue("username", username));
        return this.repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Fetch Brand by id {} by user {} failed - not found",
                            id,
                            keyValue("username", username)
                    );
                    return new ResourceNotFoundException("Brand with id={%s} not found!".formatted(id));
                });
    }

    public BrandEntity save(BrandEntity brand) {
        String username = getUsername();
        log.info("Attempt to save Brand {} by user {}", brand, keyValue("username", username));

        if (this.repository.existsByName(brand.getName())) {
            log.warn("Attempt to save Brand {} with duplicate name by user {}",
                    brand,
                    keyValue("username", username)
            );
            throw new DuplicateResourceException("Brand with name={%s} already exists!"
                    .formatted(brand.getName()));
        }

        BrandEntity savedBrand = this.repository.save(brand);
        log.info("Brand {} successfully saved by user {}", brand, keyValue("username", username));
        return savedBrand;
    }

    public String deleteById(Integer id) {
        String username = getUsername();
        log.info("Attempt to soft-delete Brand by id {} by user {}",
                id,
                keyValue("username", username)
        );

        this.repository.deleteById(id);
        return "Entity with id={%s} successfully deleted (or ignored if it did not exist in the first place)!"
                .formatted(id);
    }

    public BrandEntity update(BrandEntity brand) {
        String username = getUsername();
        log.info("Attempt to update Brand {}, by user {}", brand, keyValue("username", username));

        // corner cases to prevent update queries to db
        // that maybe create more performance issues than mitigate
        // TODO: check under load
        if (!this.repository.existsById(brand.getId())) {
            log.warn("Attempt to update non-existent Brand {}, by user {}",
                    brand, keyValue("username", username));
            throw new ResourceNotFoundException("Brand with id={%s} not found!".formatted(brand.getId()));
        }
        BrandEntity currentBrand = this.repository.findById(brand.getId()).orElse(null);
        if (brand.equals(currentBrand)) {
            log.warn("Attempt to update Brand {} with identical data by user {}",
                    brand, keyValue("username", username));
            return brand;
        }
        if (!this.repository.findAllByName(brand.getName())
                .stream()
                .filter(b -> !Objects.equals(b.getId(), brand.getId()))
                .toList()
                .isEmpty()
        ) {
            log.warn("Attempt to save Brand with duplicate name {} by user {}",
                    brand, keyValue("username", username));
            throw new DuplicateResourceException("Brand with name={%s} already exists"
                    .formatted(brand.getName()));
        }
        brand.setDateAdded(Objects.nonNull(currentBrand.getDateAdded())
                ? currentBrand.getDateAdded()
                : new Timestamp(System.currentTimeMillis()));
        BrandEntity savedBrand = this.repository.save(brand);
        log.info("Brand {} successfully updated by user {}",
                savedBrand, keyValue("username", username));
        return savedBrand;
    }

    public BrandEntity findByName(String name) {
        return this.repository.findOneByName(name);
    }

    public Collection<BrandEntity> saveAll(Collection<BrandEntity> brands) {
        return this.repository.saveAll(brands);
    }

    public Page<BrandEntity> getAll(Pageable pageable) {
        String username = getUsername();
        log.info("Attempt to fetch all Brands with pageable {} by user {}",
                pageable, keyValue("username", username));
        try {
            Page<BrandEntity> brands = this.repository.findAll(pageable);
            log.info("Brands successfully fetched by user {}", keyValue("username", username));
            return brands;
        } catch (PropertyReferenceException e) {
            log.error("Exception fetching Brands by user {}", keyValue("username", username));
            log.error("Message: {}", e.getMessage());
            log.debug("Stacktrace: {}", Arrays.toString(e.getStackTrace()));
            throw new BadRequestException(e.getMessage());
        }
    }
}
