package com.maxzamota.spring_sandbox.mappers;

import com.maxzamota.spring_sandbox.model.CustomerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper implements EntityMapper<com.maxzamota.spring_sandbox.dto.CustomerDto, CustomerEntity> {

    private final ModelMapper mapper;

    @Autowired
    public CustomerMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public com.maxzamota.spring_sandbox.dto.CustomerDto toDto(CustomerEntity entity) {
        return mapper.map(entity, com.maxzamota.spring_sandbox.dto.CustomerDto.class);
    }

    @Override
    public CustomerEntity fromDto(com.maxzamota.spring_sandbox.dto.CustomerDto dto) {
        return mapper.map(dto, CustomerEntity.class);
    }
}
