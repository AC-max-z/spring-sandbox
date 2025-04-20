package com.maxzamota.spring_sandbox.model.model_assemblers;

import org.springframework.hateoas.EntityModel;

public interface DtoAssembler<V> {
    EntityModel<V> toDtoModel(V dto);
}
