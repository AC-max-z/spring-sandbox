package com.maxzamota.spring_sandbox.service;

import com.maxzamota.spring_sandbox.exception.BadRequestException;
import com.maxzamota.spring_sandbox.exception.DuplicateResourceException;
import com.maxzamota.spring_sandbox.exception.ResourceNotFoundException;
import com.maxzamota.spring_sandbox.mappers.UserMapper;
import com.maxzamota.spring_sandbox.model.UserEntity;
import com.maxzamota.spring_sandbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService {
    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
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
        Page<UserEntity> users;
        try {
            users = this.repository.findAll(pageable);
            return users;
        } catch (PropertyReferenceException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public UserEntity getById(int id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id={%s} not found!".formatted(id))
                );
    }

    public Collection<UserEntity> getByEmail(String email) {
        return repository.findByEmail(email);
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
