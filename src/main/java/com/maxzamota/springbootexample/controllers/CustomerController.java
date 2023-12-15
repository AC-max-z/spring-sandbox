package com.maxzamota.springbootexample.controllers;

import com.maxzamota.springbootexample.dto.CustomerDto;
import com.maxzamota.springbootexample.enums.CustomerSortType;
import com.maxzamota.springbootexample.model.Customer;
import com.maxzamota.springbootexample.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

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
        Collection<Customer> customers = this.customerService.getAllCustomers();
        if (Objects.nonNull(sortType)) {
            CustomerSortType customerSortType = CustomerSortType.BY_ID_ASC;
            try {
                customerSortType = CustomerSortType.valueOf(sortType);
            } catch (IllegalArgumentException ignored) {
            } finally {
                customers = this.customerService.sortedCustomers(customers, customerSortType);
            }
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
