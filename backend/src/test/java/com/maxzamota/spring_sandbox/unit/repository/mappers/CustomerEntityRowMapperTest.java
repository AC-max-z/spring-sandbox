package com.maxzamota.spring_sandbox.unit.repository.mappers;

import com.maxzamota.spring_sandbox.util.generators.CustomerGenerator;
import com.maxzamota.spring_sandbox.model.CustomerEntity;
import com.maxzamota.spring_sandbox.repository.mappers.CustomerRowMapper;
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
class CustomerEntityRowMapperTest {

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
        CustomerEntity expectedCustomerEntity = new CustomerGenerator()
                .withId(1)
                .withName("Jamal")
                .withEmail("bigBallsJamal@yahoo.com")
                .withAge(42)
                .generate();
        when(rs.getInt("id")).thenReturn(expectedCustomerEntity.getId());
        when(rs.getString("name")).thenReturn(expectedCustomerEntity.getName());
        when(rs.getString("email")).thenReturn(expectedCustomerEntity.getEmail());
        when(rs.getInt("age")).thenReturn(expectedCustomerEntity.getAge());
        when(rs.getString("gender")).thenReturn(String.valueOf(expectedCustomerEntity.getGender()));
        // Act
        CustomerEntity customerEntity = customerRowMapper.mapRow(rs, 1);
        // Assert
        assertThat(customerEntity).usingRecursiveComparison().isEqualTo(expectedCustomerEntity);
    }
}