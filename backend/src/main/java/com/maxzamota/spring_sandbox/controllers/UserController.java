package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.model.model_assemblers.UserModelAssembler;
import com.maxzamota.spring_sandbox.dto.UserDto;
import com.maxzamota.spring_sandbox.mappers.UserMapper;
import com.maxzamota.spring_sandbox.model.UserEntity;
import com.maxzamota.spring_sandbox.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends EntityController<Integer, UserEntity, UserDto> {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(
            UserService service,
            UserMapper mapper,
            UserModelAssembler assembler,
            UserModelAssembler dtoAssembler,
            PagedResourcesAssembler<UserEntity> pagedResourcesAssembler,
            PasswordEncoder passwordEncoder
    ) {
        super(service, mapper, assembler, dtoAssembler, pagedResourcesAssembler);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @PostAuthorize("returnObject.body.content.email == authentication.principal.username")
    public ResponseEntity<EntityModel<UserDto>> get(@PathVariable Integer id) {
        UserEntity user = service.getById(id);
        UserDto dto = this.mapper.toDto(user);
        dto.setPassword("*****");
        EntityModel<UserDto> dtoModel = dtoModelAssembler.toDtoModel(dto);
        return ResponseEntity.ok(dtoModel);
    }

    @Override
    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> post(@Valid @RequestBody UserDto userDto) {
        UserEntity user = mapper.fromDto(userDto);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = service.save(user);
        UserDto dto = this.mapper.toDto(user);
        dto.setPassword("*****");

        EntityModel<UserDto> userDtoModel = dtoModelAssembler.toDtoModel(dto);

        return ResponseEntity
                .created(userDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(userDtoModel);
    }

    @Override
//    @PreAuthorize("#id == userService.getByEmail(authentication.principal.username).get().getId()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        UserService userService = (UserService) service;
        if (!id.equals(Objects.requireNonNull(
                        userService.getByEmail(getUsername()).orElse(null))
                .getId())
        ) {
            throw new AccessDeniedException("Access is denied");
        }
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
//    @PreAuthorize("#id != userService.getByEmail(authentication.principal.username).get().getId()")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> put(
            @PathVariable Integer id,
            @Valid @RequestBody UserDto userDto
    ) {
        UserService userService = (UserService) service;
        if (!id.equals(Objects.requireNonNull(
                        userService.getByEmail(getUsername()).orElse(null))
                .getId())
        ) {
            throw new AccessDeniedException("Access is denied");
        }
        // TODO:
        return null;
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
