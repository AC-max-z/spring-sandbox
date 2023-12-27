package com.maxzamota.spring_sandbox.controllers;

import com.maxzamota.spring_sandbox.dto.CustomerDto;
import com.maxzamota.spring_sandbox.enums.CustomerSortType;
import com.maxzamota.spring_sandbox.model.Customer;
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
public class CustomerController implements EntityController<Integer, Customer, CustomerDto> {
    private final CustomerService customerService;
    private final ModelMapper mapper;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper mapper) {
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping({"/all", "list"})
    public ResponseEntity<Collection<Customer>> getAll(
            @RequestParam(required = false, name = "sort") String sortType
    ) {
        Collection<Customer> customers;
        CustomerSortType customerSortType = CustomerSortType.BY_ID_ASC;
        try {
            customerSortType = CustomerSortType.valueOf(sortType);
        } catch (IllegalArgumentException | NullPointerException ignored) {
        } finally {
            customers = this.customerService.sortedCustomers(customerSortType);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Customer> get(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(this.customerService.getCustomerById(id), HttpStatus.OK);
    }

    @Override
    @PostMapping
    public ResponseEntity<Customer> post(@RequestBody CustomerDto customerDto) {
        var customer = this.mapper.map(customerDto, Customer.class);
        return new ResponseEntity<>(this.customerService.save(customer), HttpStatus.CREATED);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(this.customerService.deleteById(id), HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Integer id, @RequestBody CustomerDto customerDto) {
        var customer = mapper.map(customerDto, Customer.class);
        customer.setId(id);
        return new ResponseEntity<>(this.customerService.updateById(customer), HttpStatus.OK);
    }
}
