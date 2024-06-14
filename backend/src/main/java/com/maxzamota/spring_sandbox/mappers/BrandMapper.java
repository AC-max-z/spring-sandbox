package com.maxzamota.spring_sandbox.mappers;

import com.maxzamota.spring_sandbox.dto.BrandDto;
import com.maxzamota.spring_sandbox.model.BrandEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrandMapper {

    private final ModelMapper mapper;

    @Autowired
    public BrandMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public BrandDto toDto(BrandEntity brandEntity) {
        return mapper.map(brandEntity, BrandDto.class);
    }

    public BrandEntity fromDto(BrandDto brandDto) {
        return mapper.map(brandDto, BrandEntity.class);
    }

    public List<BrandDto> toDtoList(List<BrandEntity> brandEntities) {
        return brandEntities.stream()
                .map(this::toDto)
                .toList();
    }

    public List<BrandEntity> fromDtoList(List<BrandDto> brandDtos) {
        return brandDtos.stream()
                .map(this::fromDto)
                .toList();
    }
}
