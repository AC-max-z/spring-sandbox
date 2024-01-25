package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.dto.BrandDto;
import com.maxzamota.spring_sandbox.enums.BrandSortType;
import com.maxzamota.spring_sandbox.exception.BadRequestException;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.service.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController implements EntityController<Integer, BrandEntity, BrandDto> {

    private final BrandService brandService;
    private final ModelMapper mapper;

    @Autowired
    public BrandController(BrandService brandService, ModelMapper mapper) {
        this.brandService = brandService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping({"/all", "/list"})
    public ResponseEntity<Collection<BrandEntity>> getAll(
            @RequestParam(required = false, name = "sort") String sortType
    ) {
        Collection<BrandEntity> brands;
        BrandSortType brandSortType = BrandSortType.BY_ID_ASC;
        try {
            brandSortType = BrandSortType.valueOf(sortType);
        } catch (IllegalArgumentException | NullPointerException ignored) {
        } finally {
            brands = this.brandService.getSortedBrands(brandSortType);
        }
        return ResponseEntity.ok(brands);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BrandEntity> get(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.brandService.getBrandById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<BrandEntity> post(@RequestBody BrandDto brandDto) {
        BrandEntity brand;
        try {
            brand = this.mapper.map(brandDto, BrandEntity.class);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        brand.setDateAdded(new Timestamp(System.currentTimeMillis()));
        brand = this.brandService.save(brand);
        return new ResponseEntity<>(brand, HttpStatus.CREATED);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id) {
        return new ResponseEntity<>(this.brandService.deleteById(id), HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<BrandEntity> update(
            @PathVariable Integer id,
            @RequestBody BrandDto brandDto
    ) {
        BrandEntity brand;
        try {
            brand = this.mapper.map(brandDto, BrandEntity.class);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        brand.setId(id);
        return new ResponseEntity<>(this.brandService.update(brand), HttpStatus.OK);
    }
}
