package com.maxzamota.spring_sandbox.model.model_assemblers;

import com.maxzamota.spring_sandbox.controllers.ProductController;
import com.maxzamota.spring_sandbox.dto.ProductDto;
import com.maxzamota.spring_sandbox.model.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements
        RepresentationModelAssembler<ProductEntity, EntityModel<ProductEntity>>,
        DtoAssembler<ProductDto> {
    @Override
    public CollectionModel<EntityModel<ProductEntity>> toCollectionModel(Iterable<? extends ProductEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    @Override
    public EntityModel<ProductEntity> toModel(ProductEntity entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(ProductController.class).get(entity.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAll(Pageable.ofSize(10))).withRel("all")
        );
    }

    @Override
    public EntityModel<ProductDto> toDtoModel(ProductDto dto) {
        return EntityModel.of(
                dto,
                linkTo(methodOn(ProductController.class).get(dto.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAll(Pageable.ofSize(10))).withRel("all")
        );
    }
}
