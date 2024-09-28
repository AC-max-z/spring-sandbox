package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.model.model_assemblers.BrandModelAssembler;
import com.maxzamota.spring_sandbox.dto.BrandDto;
import com.maxzamota.spring_sandbox.mappers.BrandMapper;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.IanaLinkRelations;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController implements EntityController<Integer, BrandEntity, BrandDto> {

    private final BrandService brandService;
    private final BrandMapper mapper;
    private final BrandModelAssembler assembler;
    private final PagedResourcesAssembler<BrandEntity> pagedAssembler;

    @Autowired
    public BrandController(
            BrandService brandService,
            BrandMapper mapper,
            BrandModelAssembler assembler,
            PagedResourcesAssembler<BrandEntity> pagedAssembler
    ) {
        this.brandService = brandService;
        this.mapper = mapper;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_user') || hasRole('ROLE_admin')")
    @GetMapping({"/all", "/list"})
    public ResponseEntity<PagedModel<EntityModel<BrandEntity>>> getAll(
            @PageableDefault(page = 0, size = 100, sort = "id")
            Pageable pageable
    ) {
        Page<BrandEntity> brands = brandService.getAll(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(brands.getNumber()));
        headers.add("X-Page-Size", String.valueOf(brands.getSize()));
        PagedModel<EntityModel<BrandEntity>> pagedModel = pagedAssembler.toModel(
                brands, assembler
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pagedModel);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_user') || hasRole('ROLE_admin')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BrandDto>> get(@PathVariable("id") Integer id) {
        BrandEntity brand = this.brandService.getBrandByIdOrThrow(id);
        BrandDto brandDto = this.mapper.toDto(brand);
        return ResponseEntity.ok(this.assembler.toDtoModel(brandDto));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping
    public ResponseEntity<EntityModel<BrandDto>> post(@RequestBody BrandDto brandDto) {
        BrandEntity brand = this.mapper.fromDto(brandDto);

        brand.setDateAdded(new Timestamp(System.currentTimeMillis()));
        brand.setId(null);
        brand = this.brandService.save(brand);
        BrandDto dto = this.mapper.toDto(brand);
        EntityModel<BrandDto> brandDtoModel = assembler.toDtoModel(dto);

        return ResponseEntity
                .created(brandDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(brandDtoModel);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        this.brandService.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<BrandDto>> update(
            @PathVariable Integer id,
            @RequestBody BrandDto brandDto
    ) {
        BrandEntity brand = this.mapper.fromDto(brandDto);

        brand.setId(id);
        brand = this.brandService.update(brand);
        BrandDto dto = this.mapper.toDto(brand);
        EntityModel<BrandDto> brandDtoModel = assembler.toDtoModel(dto);

        return ResponseEntity
                .created(brandDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(brandDtoModel);
    }
}
