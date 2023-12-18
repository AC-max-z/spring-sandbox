package com.maxzamota.springbootexample.unit.repository.mappers;

import com.maxzamota.springbootexample.generators.CustomerGenerator;
import com.maxzamota.springbootexample.model.Customer;
import com.maxzamota.springbootexample.repository.mappers.CustomerRowMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Epic("Mappers unit tests")
@Severity(SeverityLevel.BLOCKER)
class CustomerRowMapperTest {

    @Test
    @DisplayName("Should map Customer object from result set")
    @Tags({
            @Tag("unit-test"),
            @Tag("mapper"),
            @Tag("positive")
    })
    void mapRow() throws SQLException {
        Allure.suite("Mappers unit tests");
        // Arrange
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet rs = mock(ResultSet.class);
        Customer expectedCustomer = new CustomerGenerator()
                .withId(1)
                .withName("Jamal")
                .withEmail("bigBallsJamal@yahoo.com")
                .withAge(42)
                .generate();
        when(rs.getInt("id")).thenReturn(expectedCustomer.getId());
        when(rs.getString("name")).thenReturn(expectedCustomer.getName());
        when(rs.getString("email")).thenReturn(expectedCustomer.getEmail());
        when(rs.getInt("age")).thenReturn(expectedCustomer.getAge());
        // Act
        Customer customer = customerRowMapper.mapRow(rs, 1);
        // Assert
        assertThat(customer).usingRecursiveComparison().isEqualTo(expectedCustomer);
    }
}