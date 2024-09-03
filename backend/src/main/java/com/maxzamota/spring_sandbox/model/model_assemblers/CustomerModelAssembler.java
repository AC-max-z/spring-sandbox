package com.maxzamota.spring_sandbox.model.model_assemblers;

import com.maxzamota.spring_sandbox.controllers.CustomerController;
import com.maxzamota.spring_sandbox.dto.CustomerDto;
import com.maxzamota.spring_sandbox.model.CustomerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<CustomerEntity, EntityModel<CustomerEntity>> {
    @Override
    public CollectionModel<EntityModel<CustomerEntity>> toCollectionModel(Iterable<? extends CustomerEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    @Override
    public EntityModel<CustomerEntity> toModel(CustomerEntity entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(CustomerController.class).get(entity.getId())).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAll(Pageable.ofSize(10))).withRel("all")
        );
    }

    public EntityModel<CustomerDto> toDtoModel(CustomerDto dto) {
        return EntityModel.of(
                dto,
                linkTo(methodOn(CustomerController.class).get(dto.getId())).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAll(Pageable.ofSize(10))).withRel("all")
        );
    }
}
