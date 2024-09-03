package com.maxzamota.spring_sandbox.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public interface EntityController<T, E, V> {

    ResponseEntity<PagedModel<EntityModel<E>>> getAll(
            @PageableDefault Pageable pageable
    );

    ResponseEntity<EntityModel<V>> get(@PathVariable("id") T id);

    ResponseEntity<EntityModel<V>> post(V dto);

    ResponseEntity<?> deleteById(T id);

    ResponseEntity<EntityModel<V>> update(T id, V dto);
}
