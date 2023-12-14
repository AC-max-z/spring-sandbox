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

    public Collection<Customer> sortedCustomers(Collection<Customer> customers, CustomerSortType sortType) {
        return switch (sortType) {
            case CustomerSortType.BY_AGE_ASC -> customers.stream()
                    .sorted(
                            Comparator.comparingInt(Customer::getAge)
                                    .thenComparing(Customer::getId)
                                    .thenComparing(Customer::getName)
                    ).toList();
            case CustomerSortType.BY_AGE_DESC -> customers.stream()
                    .sorted(
                            Comparator.comparingInt(Customer::getAge).reversed()
                                    .thenComparing(Customer::getId)
                                    .thenComparing(Customer::getName)
                    ).toList();
            case CustomerSortType.BY_ID_ASC -> customers.stream()
                    .sorted(
                            Comparator.comparingInt(Customer::getId)
                                    .thenComparing(Customer::getAge)
                                    .thenComparing(Customer::getName)
                    ).toList();
            case CustomerSortType.BY_ID_DESC -> customers.stream()
                    .sorted(
                            Comparator.comparingInt(Customer::getId).reversed()
                                    .thenComparing(Customer::getAge)
                                    .thenComparing(Customer::getName)
                    ).toList();
            case BY_NAME_ASC -> customers.stream()
                    .sorted(
                            Comparator.comparing(Customer::getName)
                                    .thenComparing(Customer::getId)
                                    .thenComparing(Customer::getAge)
                    ).toList();
            case BY_NAME_DESC -> customers.stream()
                    .sorted(
                            Comparator.comparing(Customer::getName).reversed()
                                    .thenComparing(Customer::getId)
                                    .thenComparing(Customer::getAge)
                    ).toList();
        };
    }

    public Customer getCustomerById(Integer id) {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id={%s} not found!".formatted(id)));
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
            throw new ResourceNotFoundException("Customer with id={%s} not found!".formatted(customer.getId()));
        }
        if (customer.equals(customerRepository.findById(customer.getId()))) {
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
