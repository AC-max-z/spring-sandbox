package com.maxzamota.spring_sandbox.mappers;

import com.maxzamota.spring_sandbox.dto.BrandDto;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper implements EntityMapper<BrandDto, BrandEntity> {

    private final ModelMapper mapper;

    @Autowired
    public BrandMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public BrandDto toDto(BrandEntity brandEntity) {
        return mapper.map(brandEntity, BrandDto.class);
    }

    @Override
    public BrandEntity fromDto(BrandDto brandDto) {
        return mapper.map(brandDto, BrandEntity.class);
    }

}
