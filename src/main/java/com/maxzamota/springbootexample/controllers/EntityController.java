package com.maxzamota.springbootexample.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public interface EntityController<ID, T, DTO> {

    ResponseEntity<Collection<T>> getAll(@RequestParam String sortType);

    ResponseEntity<T> get(@PathVariable("id") ID id);

    ResponseEntity<T> post(DTO dto);

    ResponseEntity<String> deleteById(ID id);

    ResponseEntity<T> update(ID id, DTO dto);
}
