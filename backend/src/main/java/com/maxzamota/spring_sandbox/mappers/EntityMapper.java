package com.maxzamota.spring_sandbox.mappers;

import java.util.List;

public interface EntityMapper<T, E> {
    T toDto(E entity);

    E fromDto(T dto);

    default List<T> toDtoList(List<E> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    default List<E> fromDtoList(List<T> dtos) {
        return dtos.stream()
                .map(this::fromDto)
                .toList();
    }
}
