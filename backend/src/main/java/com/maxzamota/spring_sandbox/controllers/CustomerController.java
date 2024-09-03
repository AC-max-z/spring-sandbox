package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.model.model_assemblers.CustomerModelAssembler;
import com.maxzamota.spring_sandbox.dto.CustomerDto;
import com.maxzamota.spring_sandbox.exception.BadRequestException;
import com.maxzamota.spring_sandbox.mappers.CustomerMapper;
import com.maxzamota.spring_sandbox.model.CustomerEntity;
import com.maxzamota.spring_sandbox.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CustomerController implements EntityController<Integer, CustomerEntity, CustomerDto> {
    private final CustomerService customerService;
    private final CustomerMapper mapper;
    private final CustomerModelAssembler assembler;
    private final PagedResourcesAssembler<CustomerEntity> pagedAssembler;

    @Autowired
    public CustomerController(
            CustomerService customerService,
            CustomerMapper mapper,
            CustomerModelAssembler assembler,
            PagedResourcesAssembler<CustomerEntity> pagedAssembler
    ) {
        this.customerService = customerService;
        this.mapper = mapper;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Override
    @GetMapping({"/all", "list"})
    public ResponseEntity<PagedModel<EntityModel<CustomerEntity>>> getAll(
            @PageableDefault Pageable pageable
    ) {
        Page<CustomerEntity> customers = customerService.getAll(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(customers.getNumber()));
        headers.add("X-Page-Size", String.valueOf(customers.getSize()));
        PagedModel<EntityModel<CustomerEntity>> pagedModel = pagedAssembler.toModel(
                customers, assembler
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pagedModel);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CustomerDto>> get(@PathVariable("id") Integer id) {
        CustomerEntity customerEntity = this.customerService.getById(id);
        CustomerDto dto = this.mapper.toDto(customerEntity);
        EntityModel<CustomerDto> customerDtoEntityModel = assembler.toDtoModel(dto);
        return ResponseEntity.ok(customerDtoEntityModel);
    }

    @Override
    @PostMapping
    public ResponseEntity<EntityModel<CustomerDto>> post(@RequestBody CustomerDto customerDto) {
        CustomerEntity customer;

        try {
            customer = this.mapper.fromDto(customerDto);
        } catch (Exception e) {
            throw new BadRequestException(e.getCause().getMessage());
        }

        CustomerDto dto = this.mapper.toDto(customer);
        EntityModel<CustomerDto> customerDtoModel = assembler.toDtoModel(dto);

        return ResponseEntity
                .created(customerDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(customerDtoModel);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        this.customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CustomerDto>> update(
            @PathVariable Integer id,
            @RequestBody CustomerDto customerDto
    ) {
        CustomerEntity customerEntity;

        try {
            customerEntity = this.mapper.fromDto(customerDto);
        } catch (Exception e) {
            throw new BadRequestException(e.getCause().getMessage());
        }

        customerEntity.setId(id);
        customerEntity = customerService.update(customerEntity);
        CustomerDto dto = this.mapper.toDto(customerEntity);

        EntityModel<CustomerDto> customerDtoModel = assembler.toDtoModel(dto);

        return ResponseEntity
                .created(customerDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(customerDtoModel);
    }
}
