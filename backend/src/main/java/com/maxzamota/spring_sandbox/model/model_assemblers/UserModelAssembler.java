package com.maxzamota.spring_sandbox.model.model_assemblers;

import com.maxzamota.spring_sandbox.controllers.UserController;
import com.maxzamota.spring_sandbox.dto.UserDto;
import com.maxzamota.spring_sandbox.model.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements
        RepresentationModelAssembler<UserEntity, EntityModel<UserEntity>>,
        DtoAssembler<UserDto> {
    @Override
    public CollectionModel<EntityModel<UserEntity>> toCollectionModel(Iterable<? extends UserEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    @Override
    public EntityModel<UserEntity> toModel(UserEntity entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(UserController.class).get(entity.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll(Pageable.ofSize(10))).withRel("all")
        );
    }

    @Override
    public EntityModel<UserDto> toDtoModel(UserDto dto) {
        return EntityModel.of(
                dto,
                linkTo(methodOn(UserController.class).get(dto.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll(Pageable.ofSize(10))).withRel("all")
        );
    }
}
