package com.otus.network.controller;

import com.otus.network.config.security.JwtService;
import com.otus.network.library.api.internal.controller.LoginApiDelegate;
import com.otus.network.library.api.internal.model.LoginPost200Response;
import com.otus.network.library.api.internal.model.LoginPostRequest;
import com.otus.network.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginController implements LoginApiDelegate {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public ResponseEntity<LoginPost200Response> loginPost(LoginPostRequest dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getId(),
                dto.getPassword()
        ));

        var user = userService.userDetailsService().loadUserByUsername(dto.getId());

        var jwt = jwtService.generateToken(user);
        LoginPost200Response response = new LoginPost200Response();
        response.setToken(jwt);
        return ResponseEntity.ok(response);

    }
}
