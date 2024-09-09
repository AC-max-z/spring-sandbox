package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.model.model_assemblers.UserModelAssembler;
import com.maxzamota.spring_sandbox.dto.UserDto;
import com.maxzamota.spring_sandbox.mappers.UserMapper;
import com.maxzamota.spring_sandbox.model.UserEntity;
import com.maxzamota.spring_sandbox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController implements EntityController<Integer, UserEntity, UserDto> {
    private final UserService userService;
    private final UserMapper mapper;
    private final UserModelAssembler assembler;
    private final PagedResourcesAssembler<UserEntity> pagedAssembler;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(
            UserService userService,
            UserMapper mapper,
            UserModelAssembler assembler,
            PagedResourcesAssembler<UserEntity> pagedAssembler,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.mapper = mapper;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @GetMapping({"/all", "/list"})
    public ResponseEntity<PagedModel<EntityModel<UserEntity>>> getAll(
            @PageableDefault Pageable pageable
    ) {
        Page<UserEntity> users = userService.findAll(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(users.getNumber()));
        headers.add("X-Page-Size", String.valueOf(users.getSize()));
        PagedModel<EntityModel<UserEntity>> pagedModel = pagedAssembler.toModel(
                users, assembler
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pagedModel);
    }

    @Override
    @PostAuthorize("returnObject.body.content.email == authentication.principal.username")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> get(@PathVariable Integer id) {
        UserEntity user = userService.getById(id);
        UserDto dto = this.mapper.toDto(user);
        dto.setPassword("*****");
        EntityModel<UserDto> dtoModel = assembler.toDtoModel(dto);
        return ResponseEntity.ok(dtoModel);
    }

    @Override
    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> post(@RequestBody UserDto userDto) {
        UserEntity user = mapper.fromDto(userDto);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = userService.save(user);
        UserDto dto = this.mapper.toDto(user);
        dto.setPassword("*****");

        EntityModel<UserDto> userDtoModel = assembler.toDtoModel(dto);

        return ResponseEntity
                .created(
                        userDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri()
                )
                .body(userDtoModel);
    }

    @Override
//    @PreAuthorize("#id != userService.getByEmail(authentication.principal.username).get().getId()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        this.userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
//    @PreAuthorize("#id != userService.getByEmail(authentication.principal.username).get().getId()")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> update(
            @PathVariable Integer id,
            @RequestBody UserDto userDto
    ) {
        return null;
    }
}
