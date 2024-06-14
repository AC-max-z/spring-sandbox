package com.maxzamota.spring_sandbox.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public interface EntityController<ID, T, DTO> {

    ResponseEntity<PagedModel<EntityModel<T>>> getAll(
            @PageableDefault Pageable pageable
    );

    ResponseEntity<EntityModel<T>> get(@PathVariable("id") ID id);

    ResponseEntity<EntityModel<T>> post(DTO dto);

    ResponseEntity<?> deleteById(ID id);

    ResponseEntity<EntityModel<T>> update(ID id, DTO dto);
}
