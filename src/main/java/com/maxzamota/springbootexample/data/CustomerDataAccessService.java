package com.maxzamota.springbootexample.data;

import com.maxzamota.springbootexample.model.Customer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository("mock-data")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CustomerDataAccessService {

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        var alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com",
                21
        );
        var jamila = new Customer(
                2,
                "Jamila",
                "jamila@gmail.com",
                19
        );
        customers.add(alex);
        customers.add(jamila);
    }

    public Collection<Customer> selectAll() {
        return customers;
    }

    public Optional<Customer> selectById(Integer id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    public Customer save(Customer customer) {
        customers.add(customer);
        return customer;
    }
}
