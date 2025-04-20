package com.maxzamota.spring_sandbox.service;

import com.maxzamota.spring_sandbox.exception.BadRequestException;
import com.maxzamota.spring_sandbox.exception.DuplicateResourceException;
import com.maxzamota.spring_sandbox.exception.ResourceNotFoundException;
import com.maxzamota.spring_sandbox.model.UserEntity;
import com.maxzamota.spring_sandbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService implements EntityService<Integer, UserEntity> {
    private final UserRepository REPO;

    @Autowired
    public UserService(UserRepository repository) {
        this.REPO = repository;
    }

    @Override
    public UserEntity getById(Integer id) {
        return REPO.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id={%s} not found!".formatted(id))
                );
    }

    public Collection<UserEntity> getAllByEmail(String email) {
        return REPO.findAllByEmail(email);
    }

    public Optional<UserEntity> getByEmail(String email) {
        return REPO.findByEmail(email);
    }

    @Override
    public UserEntity save(UserEntity user) {
        if (REPO.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Entity with email={%s} already exists"
                    .formatted(user.getEmail()));
        }
        return REPO.save(user);
    }

    @Override
    public UserEntity update(UserEntity entity) {
        return null;
    }

    @Override
    public Page<UserEntity> getAll(Pageable pageable) {
        try {
            return this.REPO.findAll(pageable);
        } catch (PropertyReferenceException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public String deleteById(Integer id) {
        this.REPO.deleteById(id);
        return "Entity with id={%s} is deleted or ignored if it did not exist".formatted(id);
    }
}
