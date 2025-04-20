package com.maxzamota.spring_sandbox.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntityService<T, E> {
    E getById(T id);
    Page<E> getAll(Pageable pageable);
    E save(E entity);
    E update(E entity);
    String deleteById(T id);
}
