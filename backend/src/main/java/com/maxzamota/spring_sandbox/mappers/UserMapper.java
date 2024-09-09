package com.maxzamota.spring_sandbox.mappers;

import com.maxzamota.spring_sandbox.dto.UserDto;
import com.maxzamota.spring_sandbox.model.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements EntityMapper<UserDto, UserEntity> {
    private final ModelMapper mapper;

    @Autowired
    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDto toDto(UserEntity userEntity) {
        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserEntity fromDto(UserDto userDto) {
        return mapper.map(userDto, UserEntity.class);
    }
}
