package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.mappers.EntityMapper;
import com.maxzamota.spring_sandbox.model.model_assemblers.DtoAssembler;
import com.maxzamota.spring_sandbox.service.EntityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public abstract class EntityController<T, E, V> {
    // T - id type, E - entity type, V - dto type
    protected final EntityService<T, E> service;
    protected final EntityMapper<V, E> mapper;
    protected final RepresentationModelAssembler<E, EntityModel<E>> modelAssembler;
    protected final DtoAssembler<V> dtoModelAssembler;
    protected final PagedResourcesAssembler<E> pagedResourcesAssembler;

    public EntityController(
            EntityService<T, E> service,
            EntityMapper<V, E> mapper,
            RepresentationModelAssembler<E, EntityModel<E>> modelAssembler,
            DtoAssembler<V> dtoModelAssembler,
            PagedResourcesAssembler<E> pagedResourcesAssembler
    ) {
        this.service = service;
        this.mapper = mapper;
        this.modelAssembler = modelAssembler;
        this.dtoModelAssembler = dtoModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping({"/all", "/list"})
    public ResponseEntity<PagedModel<EntityModel<E>>> getAll(
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Page<E> entities = service.getAll(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(entities.getNumber()));
        headers.add("X-Page-Size", String.valueOf(entities.getSize()));
        PagedModel<EntityModel<E>> pagedModel = pagedResourcesAssembler.toModel(
                entities, modelAssembler
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pagedModel);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<V>> get(@PathVariable("id") T id) {
        E entity = this.service.getById(id);
        V dto = this.mapper.toDto(entity);
        return ResponseEntity.ok(this.dtoModelAssembler.toDtoModel(dto));
    }

    @PostMapping
    public ResponseEntity<EntityModel<V>> post(@Valid @RequestBody V dto) {
        E entity = this.mapper.fromDto(dto);
        entity = this.service.save(entity);
        V savedDto = this.mapper.toDto(entity);
        EntityModel<V> dtoModel = dtoModelAssembler.toDtoModel(savedDto);

        return ResponseEntity
                .created(dtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(dtoModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") T id) {
        this.service.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{id}")
    public abstract ResponseEntity<EntityModel<V>> put(@PathVariable("id") T id, @Valid @RequestBody V dto);
}
