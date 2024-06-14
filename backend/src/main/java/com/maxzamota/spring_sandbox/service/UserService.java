package com.maxzamota.spring_sandbox.service;

import com.maxzamota.spring_sandbox.exception.DuplicateResourceException;
import com.maxzamota.spring_sandbox.exception.ResourceNotFoundException;
import com.maxzamota.spring_sandbox.mappers.UserMapper;
import com.maxzamota.spring_sandbox.model.UserEntity;
import com.maxzamota.spring_sandbox.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository repository;
    private UserMapper mapper;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<UserEntity> findAll(
            Integer pageNum,
            Integer pageSize,
            String sortBy,
            String sortDirection
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return repository.findAll(pageable);
    }

    public Page<UserEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public UserEntity getById(int id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id={%s} not found!".formatted(id))
                );
    }

    public UserEntity save(UserEntity user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Entity with email={%s} already exists"
                    .formatted(user.getEmail()));
        }
        return repository.save(user);
    }

    public String deleteById(Integer id) {
        this.repository.deleteById(id);
        return "Entity with id={%s} is deleted or ignored if it did not exist".formatted(id);
    }
}
