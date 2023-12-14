package com.maxzamota.springbootexample.repository.jdbc;

import com.maxzamota.springbootexample.model.Customer;
import com.maxzamota.springbootexample.repository.CustomerDao;
import com.maxzamota.springbootexample.repository.mappers.CustomerRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    @Autowired
    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public Customer save(Customer customer) {
        if (Objects.nonNull(customer.getId()) && this.existsCustomerById(customer.getId())) {
            var updateQuery = """
                    UPDATE customer
                    SET name = ?, email = ?, age = ?
                    WHERE id = ?
                    """;
            this.jdbcTemplate.update(
                    updateQuery,
                    customer.getName(),
                    customer.getEmail(),
                    customer.getAge(),
                    customer.getId()
            );
            return customer;
        }
        var insertQuery = """
                INSERT INTO customer(name, email, age)
                VALUES (?, ?, ?)
                """;
        var selectQuery = """
                SELECT *
                FROM customer
                WHERE email = ?
                """;
        this.jdbcTemplate.update(insertQuery, customer.getName(), customer.getEmail(), customer.getAge());
        return this.jdbcTemplate.query(
                        selectQuery,
                        this.customerRowMapper,
                        customer.getEmail()
                ).stream()
                .findFirst()
                .orElse(customer);

    }

    @Override
    public Collection<Customer> findAll() {
        var query = """
                SELECT * FROM customer
                """;
        return this.jdbcTemplate.query(query, this.customerRowMapper);
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        var sql = """
                SELECT *
                FROM customer
                WHERE id = ?
                """;

        return this.jdbcTemplate.query(sql, this.customerRowMapper, id).stream()
                .findFirst();
    }

    @Override
    public boolean existsCustomerByEmail(String email) {
        var sql = """
                SELECT count(*)
                FROM customer
                WHERE email = ?
                """;
        Integer count = this.jdbcTemplate.queryForObject(sql, Integer.class, email);
        return Objects.nonNull(count) && count > 0;
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        var query = """
                SELECT count(*)
                FROM customer
                WHERE id = ?
                """;
        Integer count = this.jdbcTemplate.queryForObject(query, Integer.class, id);
        return Objects.nonNull(count) && count > 0;
    }

    @Override
    public void deleteById(Integer id) {
        var query = """
                DELETE FROM customer
                WHERE id = ?
                """;
        this.jdbcTemplate.update(query, id);
    }

    @Override
    public Collection<Customer> findCustomersByEmail(String email) {
        var query = """
                SELECT *
                FROM customer
                WHERE email = ?
                """;
        return this.jdbcTemplate.query(query, this.customerRowMapper, email);
    }
}
