package com.maxzamota.spring_sandbox.repository.jpa;

import com.maxzamota.spring_sandbox.model.CustomerEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository("jpa")
@Primary
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);
    Collection<CustomerEntity> findCustomersByEmail(String email);
    Collection<CustomerEntity> findAllByOrderByIdAsc();
    Collection<CustomerEntity> findAllByOrderByIdDesc();
    Collection<CustomerEntity> findAllByOrderByNameAsc();
    Collection<CustomerEntity> findAllByOrderByNameDesc();
    Collection<CustomerEntity> findAllByOrderByEmailAsc();
    Collection<CustomerEntity> findAllByOrderByEmailDesc();
    Collection<CustomerEntity> findAllByOrderByAgeAsc();
    Collection<CustomerEntity> findAllByOrderByAgeDesc();
}
