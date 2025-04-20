package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.dto.CustomerDto;
import com.maxzamota.spring_sandbox.model.model_assemblers.CustomerModelAssembler;
import com.maxzamota.spring_sandbox.mappers.CustomerMapper;
import com.maxzamota.spring_sandbox.model.CustomerEntity;
import com.maxzamota.spring_sandbox.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CustomerController extends EntityController<Integer, CustomerEntity, CustomerDto> {

    @Autowired
    public CustomerController(
            CustomerService service,
            CustomerMapper mapper,
            CustomerModelAssembler assembler,
            CustomerModelAssembler dtoAssembler,
            PagedResourcesAssembler<CustomerEntity> pagedResourcesAssembler
    ) {
        super(service, mapper, assembler, dtoAssembler, pagedResourcesAssembler);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CustomerDto>> put(
            @PathVariable Integer id,
            @Valid @RequestBody CustomerDto customerDto
    ) {
        CustomerEntity customerEntity = this.mapper.fromDto(customerDto);

        customerEntity.setId(id);
        customerEntity = service.update(customerEntity);
        CustomerDto dto = this.mapper.toDto(customerEntity);

        EntityModel<CustomerDto> customerDtoModel = dtoModelAssembler.toDtoModel(dto);

        return ResponseEntity
                .created(customerDtoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(customerDtoModel);
    }
}
