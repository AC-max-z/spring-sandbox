package com.maxzamota.spring_sandbox.mappers;

import com.maxzamota.spring_sandbox.dto.CustomerDto;
import com.maxzamota.spring_sandbox.model.CustomerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    private final ModelMapper mapper;

    @Autowired
    public CustomerMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CustomerDto toDto(CustomerEntity entity) {
        return mapper.map(entity, CustomerDto.class);
    }

    public CustomerEntity fromDto(CustomerDto dto) {
        return mapper.map(dto, CustomerEntity.class);
    }

    public List<CustomerDto> toDtoList(List<CustomerEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    public List<CustomerEntity> fromDtoList(List<CustomerDto> dtos) {
        return dtos.stream()
                .map(this::fromDto)
                .toList();
    }
}
