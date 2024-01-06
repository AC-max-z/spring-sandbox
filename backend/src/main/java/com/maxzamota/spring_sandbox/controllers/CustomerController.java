package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.dto.CustomerDto;
import com.maxzamota.spring_sandbox.enums.CustomerSortType;
import com.maxzamota.spring_sandbox.exception.BadRequestException;
import com.maxzamota.spring_sandbox.model.CustomerEntity;
import com.maxzamota.spring_sandbox.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/customer")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CustomerController implements EntityController<Integer, CustomerEntity, CustomerDto> {
    private final CustomerService customerService;
    private final ModelMapper mapper;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper mapper) {
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping({"/all", "list"})
    public ResponseEntity<Collection<CustomerEntity>> getAll(
            @RequestParam(required = false, name = "sort") String sortType
    ) {
        Collection<CustomerEntity> customerEntities;
        CustomerSortType customerSortType = CustomerSortType.BY_ID_ASC;
        try {
            customerSortType = CustomerSortType.valueOf(sortType);
        } catch (IllegalArgumentException | NullPointerException ignored) {
        } finally {
            customerEntities = this.customerService.sortedCustomers(customerSortType);
        }
        return new ResponseEntity<>(customerEntities, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CustomerEntity> get(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(this.customerService.getCustomerById(id), HttpStatus.OK);
    }

    @Override
    @PostMapping
    public ResponseEntity<CustomerEntity> post(@RequestBody CustomerDto customerDto) {
        CustomerEntity customer;
        try {
            customer = this.mapper.map(customerDto, CustomerEntity.class);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        return new ResponseEntity<>(this.customerService.save(customer), HttpStatus.CREATED);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(this.customerService.deleteById(id), HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CustomerEntity> update(@PathVariable Integer id, @RequestBody CustomerDto customerDto) {
        CustomerEntity customerEntity;
        try {
            customerEntity = mapper.map(customerDto, CustomerEntity.class);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        customerEntity.setId(id);
        return new ResponseEntity<>(this.customerService.updateById(customerEntity), HttpStatus.OK);
    }
}
