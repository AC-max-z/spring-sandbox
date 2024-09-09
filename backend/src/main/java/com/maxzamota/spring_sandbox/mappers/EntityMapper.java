package com.maxzamota.spring_sandbox.mappers;

import java.util.List;

public interface EntityMapper<T, E>{
    T toDto(E entity);
    E fromDto(T dto);
    List<T> toDtoList(List<E> entities);
    List<E> fromDtoList(List<T> dtos);
}
