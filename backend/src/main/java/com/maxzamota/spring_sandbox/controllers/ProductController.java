package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.dto.ProductDto;
import com.maxzamota.spring_sandbox.enums.ProductSortType;
import com.maxzamota.spring_sandbox.exception.BadRequestException;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.model.ProductEntity;
import com.maxzamota.spring_sandbox.service.BrandService;
import com.maxzamota.spring_sandbox.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController implements EntityController<Integer, ProductEntity, ProductDto> {
    private final ProductService productService;
    private final BrandService brandService;
    private final ModelMapper mapper;

    @Autowired
    public ProductController(
            ProductService productService,
            BrandService brandService,
            ModelMapper mapper
    ) {
        this.productService = productService;
        this.brandService = brandService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping({"/all", "/list"})
    public ResponseEntity<Collection<ProductEntity>> getAll(
            @RequestParam(required = false) String sortType
    ) {
        ProductSortType sort = ProductSortType.BY_ID_ASC;
        try {
            sort = ProductSortType.valueOf(sortType);
        } catch (Exception ignored) {}
        return ResponseEntity.ok(this.productService.getAllSorted(sort));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> get(@PathVariable Integer id) {
        return ResponseEntity.ok(this.productService.getById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<ProductEntity> post(@RequestBody ProductDto productDto) {
        ProductEntity product;
        try {
            BrandEntity brand = this.brandService.findBrandByName(productDto.getBrand().getName());
            product = this.mapper.map(productDto, ProductEntity.class);
            if (Objects.nonNull(brand)) {
                product.setBrand(brand);
            } else {
                brand = this.mapper.map(productDto.getBrand(), BrandEntity.class);
                brand.setDateAdded(new Timestamp(System.currentTimeMillis()));
                brand = this.brandService.save(brand);
                product.setBrand(brand);
            }
            product.setDateAdded(new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity<>(this.productService.save(product), HttpStatus.CREATED);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.productService.deleteById(id));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> update(
            @PathVariable Integer id,
            @RequestBody ProductDto productDto
    ) {
        ProductEntity product;
        try {
            BrandEntity brand = this.brandService.findBrandByName(productDto.getBrand().getName());
            product = this.mapper.map(productDto, ProductEntity.class);
            if (Objects.nonNull(brand)) {
                product.setBrand(brand);
            } else {
                brand = this.mapper.map(productDto.getBrand(), BrandEntity.class);
                brand.setDateAdded(new Timestamp(System.currentTimeMillis()));
                brand = this.brandService.save(brand);
                product.setBrand(brand);
            }
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        product.setId(id);
        return new ResponseEntity<>(this.productService.update(product), HttpStatus.OK);
    }
}
