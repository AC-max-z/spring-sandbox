package com.maxzamota.spring_sandbox.mappers;

import com.maxzamota.spring_sandbox.dto.ProductDto;
import com.maxzamota.spring_sandbox.model.ProductEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {
    private final ModelMapper mapper;

    @Autowired
    public ProductMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ProductDto toDto(ProductEntity productEntity) {
        return mapper.map(productEntity, ProductDto.class);
    }

    public ProductEntity fromDto(ProductDto productDto) {
        return mapper.map(productDto, ProductEntity.class);
    }

    public List<ProductDto> toDtoList(List<ProductEntity> productEntities) {
        return productEntities.stream()
                .map(this::toDto)
                .toList();
    }

    public List<ProductEntity> fromDtoList(List<ProductDto> productDtos) {
        return productDtos.stream()
                .map(this::fromDto)
                .toList();
    }
}
