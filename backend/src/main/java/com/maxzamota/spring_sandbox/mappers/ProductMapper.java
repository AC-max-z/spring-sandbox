package com.maxzamota.spring_sandbox.mappers;

import com.maxzamota.spring_sandbox.dto.ProductDto;
import com.maxzamota.spring_sandbox.model.ProductEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements EntityMapper<ProductDto, ProductEntity> {
    private final ModelMapper mapper;

    @Autowired
    public ProductMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ProductDto toDto(ProductEntity productEntity) {
        return mapper.map(productEntity, ProductDto.class);
    }

    @Override
    public ProductEntity fromDto(ProductDto productDto) {
        return mapper.map(productDto, ProductEntity.class);
    }

}
