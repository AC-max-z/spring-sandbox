package com.maxzamota.springbootexample.service;

import com.maxzamota.springbootexample.enums.CustomerSortType;
import com.maxzamota.springbootexample.exception.DuplicateResourceException;
import com.maxzamota.springbootexample.exception.ResourceNotFoundException;
import com.maxzamota.springbootexample.model.Customer;
import com.maxzamota.springbootexample.repository.jpa.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(@Qualifier("jpa") CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Collection<Customer> getAllCustomers() {
        return this.customerRepository.findAll();
    }

    public Collection<Customer> sortedCustomers(@NonNull CustomerSortType sortType) {
        return switch (sortType) {
            case CustomerSortType.BY_AGE_ASC -> this.customerRepository.findAllByOrderByAgeAsc();
            case CustomerSortType.BY_AGE_DESC -> this.customerRepository.findAllByOrderByAgeDesc();
            case CustomerSortType.BY_ID_ASC -> this.customerRepository.findAllByOrderByIdAsc();
            case CustomerSortType.BY_ID_DESC -> this.customerRepository.findAllByOrderByIdDesc();
            case CustomerSortType.BY_NAME_ASC -> this.customerRepository.findAllByOrderByNameAsc();
            case CustomerSortType.BY_NAME_DESC -> this.customerRepository.findAllByOrderByNameDesc();
        };
    }

    public Customer getCustomerById(Integer id) {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id={%s} not found!"
                        .formatted(id)));
    }

    public Customer save(Customer customer) {
        if (this.customerRepository.existsCustomerByEmail(customer.getEmail())) {
            throw new DuplicateResourceException(
                    "Customer with email={%s} already exists!".formatted(customer.getEmail())
            );
        }
        return this.customerRepository.save(customer);
    }

    public String deleteById(Integer id) {
        this.customerRepository.deleteById(id);
        return "Entity with id={%s} successfully deleted (or ignored if it did not exist in the first place)!"
                .formatted(id);
    }

    public Customer updateById(Customer customer) {
        if (!this.customerRepository.existsCustomerById(customer.getId())) {
            throw new ResourceNotFoundException("Customer with id={%s} not found!"
                    .formatted(customer.getId()));
        }
        if (customer.equals(
                this.customerRepository
                        .findById(customer.getId())
                        .orElseGet(() -> null))
        ) {
            return customer;
        }
        if (!this.customerRepository.findCustomersByEmail(customer.getEmail())
                .stream()
                .filter(record -> !Objects.equals(record.getId(), customer.getId()))
                .toList()
                .isEmpty()
        ) {
            throw new DuplicateResourceException("Customer with email={%s} already exists!"
                    .formatted(customer.getEmail())
            );
        }
        return this.customerRepository.save(customer);
    }
}
