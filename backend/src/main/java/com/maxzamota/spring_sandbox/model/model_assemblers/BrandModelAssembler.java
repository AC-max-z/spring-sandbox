package com.maxzamota.spring_sandbox.model.model_assemblers;

import com.maxzamota.spring_sandbox.controllers.BrandController;
import com.maxzamota.spring_sandbox.dto.BrandDto;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BrandModelAssembler implements RepresentationModelAssembler<BrandEntity, EntityModel<BrandEntity>>,DtoAssembler<BrandDto> {
    @Override
    public CollectionModel<EntityModel<BrandEntity>> toCollectionModel(Iterable<? extends BrandEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    @Override
    public EntityModel<BrandEntity> toModel(BrandEntity entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(BrandController.class).get(entity.getId())).withSelfRel(),
                linkTo(methodOn(BrandController.class).getAll(Pageable.ofSize(10))).withRel("all")
        );
    }

    @Override
    public EntityModel<BrandDto> toDtoModel(BrandDto brandDto) {
        return EntityModel.of(
                brandDto,
                linkTo(methodOn(BrandController.class).get(brandDto.getId())).withSelfRel(),
                linkTo(methodOn(BrandController.class).getAll(Pageable.ofSize(10))).withRel("all")
        );
    }
}
