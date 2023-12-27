package com.maxzamota.spring_sandbox.repository;

import com.maxzamota.spring_sandbox.model.Customer;

import java.util.Collection;
import java.util.Optional;

public interface CustomerDao {
    Customer save(Customer customer);
    Collection<Customer> findAll();
    Optional<Customer> findById(Integer id);
    boolean existsCustomerByEmail(String id);
    boolean existsCustomerById(Integer id);
    void deleteById(Integer id);
    Collection<Customer> findCustomersByEmail(String email);
    void clear();
}
