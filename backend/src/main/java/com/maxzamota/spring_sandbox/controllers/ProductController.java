package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.model.model_assemblers.ProductModelAssembler;
import com.maxzamota.spring_sandbox.dto.ProductDto;
import com.maxzamota.spring_sandbox.mappers.ProductMapper;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.model.ProductEntity;
import com.maxzamota.spring_sandbox.service.BrandService;
import com.maxzamota.spring_sandbox.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController implements EntityController<Integer, ProductEntity, ProductDto> {
    private final ProductService productService;
    private final BrandService brandService;
    private final ProductMapper mapper;
    private final ProductModelAssembler assembler;
    private final PagedResourcesAssembler<ProductEntity> pagedAssembler;

    @Autowired
    public ProductController(
            ProductService productService,
            BrandService brandService,
            ProductMapper mapper,
            ProductModelAssembler assembler,
            PagedResourcesAssembler<ProductEntity> pagedAssembler
    ) {
        this.productService = productService;
        this.brandService = brandService;
        this.mapper = mapper;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_user')")
    @GetMapping({"/all", "/list"})
    public ResponseEntity<PagedModel<EntityModel<ProductEntity>>> getAll(
            @PageableDefault Pageable pageable
    ) {
        Page<ProductEntity> products = productService.getAll(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(products.getNumber()));
        headers.add("X-Page-Size", String.valueOf(products.getSize()));
        PagedModel<EntityModel<ProductEntity>> pagedModel = pagedAssembler.toModel(
                products, assembler
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pagedModel);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_user')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductDto>> get(@PathVariable Integer id) {
        ProductEntity product = this.productService.getById(id);
        ProductDto dto = this.mapper.toDto(product);
        EntityModel<ProductDto> productDtoEntityModel = this.assembler.toDtoModel(dto);
        return ResponseEntity.ok(productDtoEntityModel);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public ResponseEntity<EntityModel<ProductDto>> post(@RequestBody ProductDto productDto) {
        ProductEntity product = this.mapper.fromDto(productDto);

        // Check if dto contains info about brand
        if (Objects.nonNull(productDto.getBrand())) {
            // check if this brand already exists
            BrandEntity brand = this.brandService.findByName(productDto.getBrand().getName());
            // create new brand if it does not exist
            if (Objects.isNull(brand)) {
                brand = product.getBrand();
                brand.setDateAdded(new Timestamp(System.currentTimeMillis()));
                brand = this.brandService.save(brand);
            }
            // update brand data with the one stored in database
            product.setBrand(brand);
        }

        product = this.productService.save(product);
        ProductDto dto = this.mapper.toDto(product);
        EntityModel<ProductDto> productDtoModel = this.assembler.toDtoModel(dto);

        return ResponseEntity
                .created(productDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productDtoModel);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        this.productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductDto>> update(
            @PathVariable Integer id,
            @RequestBody ProductDto productDto
    ) {
        ProductEntity product = this.mapper.fromDto(productDto);

        // Check if dto contains info about brand
        if (Objects.nonNull(productDto.getBrand())) {
            // check if this brand already exists
            BrandEntity brand = this.brandService.findByName(productDto.getBrand().getName());
            // create new brand if it does not exist
            if (Objects.isNull(brand)) {
                brand = product.getBrand();
                brand.setDateAdded(new Timestamp(System.currentTimeMillis()));
                brand = this.brandService.save(brand);
            }
            // update brand data with the one stored in database
            product.setBrand(brand);
        }

        product.setId(id);
        product = productService.update(product);
        ProductDto dto = this.mapper.toDto(product);
        EntityModel<ProductDto> productDtoModel = this.assembler.toDtoModel(dto);

        return ResponseEntity
                .created(productDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productDtoModel);
    }
}
