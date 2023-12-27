package com.maxzamota.spring_sandbox.repository.jpa;

import com.maxzamota.spring_sandbox.model.Customer;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository("jpa")
@Primary
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);
    Collection<Customer> findCustomersByEmail(String email);
    Collection<Customer> findAllByOrderByIdAsc();
    Collection<Customer> findAllByOrderByIdDesc();
    Collection<Customer> findAllByOrderByNameAsc();
    Collection<Customer> findAllByOrderByNameDesc();
    Collection<Customer> findAllByOrderByEmailAsc();
    Collection<Customer> findAllByOrderByEmailDesc();
    Collection<Customer> findAllByOrderByAgeAsc();
    Collection<Customer> findAllByOrderByAgeDesc();
}
