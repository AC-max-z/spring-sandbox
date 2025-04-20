package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.model.model_assemblers.ProductModelAssembler;
import com.maxzamota.spring_sandbox.dto.ProductDto;
import com.maxzamota.spring_sandbox.mappers.ProductMapper;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.model.ProductEntity;
import com.maxzamota.spring_sandbox.service.BrandService;
import com.maxzamota.spring_sandbox.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController extends EntityController<Integer, ProductEntity, ProductDto> {
    private final BrandService brandService;

    @Autowired
    public ProductController(
            ProductService service,
            ProductMapper mapper,
            ProductModelAssembler assembler,
            ProductModelAssembler dtoAssembler,
            PagedResourcesAssembler<ProductEntity> pagedResourcesAssembler,
            BrandService brandService
    ) {
        super(service, mapper, assembler, dtoAssembler, pagedResourcesAssembler);
        this.brandService = brandService;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER') || hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<ProductEntity>>> getAll(
            @PageableDefault(page = 0, size = 100, sort = "id")
            Pageable pageable
    ) {
        return super.getAll(pageable);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER') || hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<EntityModel<ProductDto>> get(@PathVariable Integer id) {
        return super.get(id);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<EntityModel<ProductDto>> post(@Valid @RequestBody ProductDto productDto) {
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

        product.setDateAdded(new Timestamp(System.currentTimeMillis()));
        product = this.service.save(product);
        ProductDto dto = this.mapper.toDto(product);
        EntityModel<ProductDto> productDtoModel = this.dtoModelAssembler.toDtoModel(dto);

        return ResponseEntity
                .created(productDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productDtoModel);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        return super.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductDto>> put(
            @PathVariable Integer id,
            @Valid @RequestBody ProductDto productDto
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
        product = service.update(product);
        ProductDto dto = this.mapper.toDto(product);
        EntityModel<ProductDto> productDtoModel = this.dtoModelAssembler.toDtoModel(dto);

        return ResponseEntity
                .created(productDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productDtoModel);
    }
}
