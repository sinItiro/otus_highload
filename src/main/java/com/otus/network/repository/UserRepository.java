package com.otus.network.repository;

import com.otus.network.entity.UserEntity;
import com.otus.network.library.api.internal.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> findById(String email);
    String save(UserEntity entity);
    void saveBatch(UserEntity entity);
    List<User> findAll();
    List<User> findByFilter(String firstName, String lastName);
    Optional<User> getById(String id);
    boolean existsById(String id);
}
