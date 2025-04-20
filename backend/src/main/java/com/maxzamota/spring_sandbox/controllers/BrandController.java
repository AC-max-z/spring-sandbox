package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.model.model_assemblers.BrandModelAssembler;
import com.maxzamota.spring_sandbox.dto.BrandDto;
import com.maxzamota.spring_sandbox.mappers.BrandMapper;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import com.maxzamota.spring_sandbox.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.IanaLinkRelations;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController extends EntityController<Integer, BrandEntity, BrandDto> {

    @Autowired
    public BrandController(
            BrandService service,
            BrandMapper mapper,
            BrandModelAssembler assembler,
            BrandModelAssembler dtoAssembler,
            PagedResourcesAssembler<BrandEntity> pagedResourcesAssembler
    ) {
        super(service, mapper, assembler, dtoAssembler, pagedResourcesAssembler);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN') || hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<BrandEntity>>> getAll(
            @PageableDefault(page = 0, size = 100, sort = "id")
            Pageable pageable
    ) {
        return super.getAll(pageable);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN') || hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<EntityModel<BrandDto>> get(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<EntityModel<BrandDto>> post(@Valid @RequestBody BrandDto brandDto) {
        BrandEntity brand = this.mapper.fromDto(brandDto);
        brand.setDateAdded(new Timestamp(System.currentTimeMillis()));
        brand.setId(null);
        brand = this.service.save(brand);
        BrandDto savedDto = this.mapper.toDto(brand);
        EntityModel<BrandDto> dtoModel = dtoModelAssembler.toDtoModel(savedDto);

        return ResponseEntity
                .created(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        return super.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<BrandDto>> put(
            @PathVariable Integer id,
            @Valid @RequestBody BrandDto brandDto
    ) {
        BrandEntity brand = this.mapper.fromDto(brandDto);

        brand.setId(id);
        brand = this.service.update(brand);
        BrandDto dto = this.mapper.toDto(brand);
        EntityModel<BrandDto> brandDtoModel = dtoModelAssembler.toDtoModel(dto);

        return ResponseEntity
                .created(brandDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(brandDtoModel);
    }
}
