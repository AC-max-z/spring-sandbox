package com.maxzamota.spring_sandbox.repository;

import com.maxzamota.spring_sandbox.model.CustomerEntity;

import java.util.Collection;
import java.util.Optional;

public interface CustomerDao {
    CustomerEntity save(CustomerEntity customerEntity);
    Collection<CustomerEntity> findAll();
    Optional<CustomerEntity> findById(Integer id);
    boolean existsCustomerByEmail(String id);
    boolean existsCustomerById(Integer id);
    void deleteById(Integer id);
    Collection<CustomerEntity> findCustomersByEmail(String email);
    void clear();
}
