package com.otus.network.controller;

import com.otus.network.library.api.internal.controller.UserApiDelegate;
import com.otus.network.library.api.internal.model.User;
import com.otus.network.library.api.internal.model.UserRegisterPost200Response;
import com.otus.network.library.api.internal.model.UserRegisterPostRequest;
import com.otus.network.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserController implements UserApiDelegate {
    private final UserService userService;

    @Override
    public ResponseEntity<User> getUserById(String id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<UserRegisterPost200Response> userRegisterPost(UserRegisterPostRequest dto) {
        UserRegisterPost200Response response = new UserRegisterPost200Response();
        response.setUserId(String.valueOf(userService.save(dto)));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<User>> searchUser(String firstName, String lastName) {
        List<User> users = userService.findByFilter(firstName, lastName);
        return ResponseEntity.ok(users);
    }
}
