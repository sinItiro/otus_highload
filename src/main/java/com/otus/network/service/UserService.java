package com.otus.network.service;

import com.otus.network.library.api.internal.model.User;
import com.otus.network.library.api.internal.model.UserRegisterPostRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    String save(UserRegisterPostRequest registrationDto);
    List<User> getAll();
    User getById(String id);
    String create(UserRegisterPostRequest registrationDto);
    User getByUsername(String username);
    UserDetailsService userDetailsService();
    User getCurrentUser();
    String register(UserRegisterPostRequest dto);

}
