package com.maxzamota.spring_sandbox.mappers;

import com.maxzamota.spring_sandbox.dto.CustomerDto;
import com.maxzamota.spring_sandbox.model.CustomerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper implements EntityMapper<CustomerDto, CustomerEntity> {

    private final ModelMapper mapper;

    @Autowired
    public CustomerMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CustomerDto toDto(CustomerEntity entity) {
        return mapper.map(entity, CustomerDto.class);
    }

    @Override
    public CustomerEntity fromDto(CustomerDto dto) {
        return mapper.map(dto, CustomerEntity.class);
    }

    @Override
    public List<CustomerDto> toDtoList(List<CustomerEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<CustomerEntity> fromDtoList(List<CustomerDto> dtos) {
        return dtos.stream()
                .map(this::fromDto)
                .toList();
    }
}
