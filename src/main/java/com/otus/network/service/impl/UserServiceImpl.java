package com.otus.network.service.impl;

import com.otus.network.config.security.JwtService;
import com.otus.network.entity.Role;
import com.otus.network.entity.UserEntity;
import com.otus.network.exception.NotFoundException;
import com.otus.network.library.api.internal.model.User;
import com.otus.network.library.api.internal.model.UserRegisterPostRequest;
import com.otus.network.repository.UserRepository;
import com.otus.network.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public String save(UserRegisterPostRequest dto) {

        UserEntity user = new UserEntity();
        user.setBirthdate(dto.getBirthdate());
        user.setCity(dto.getCity());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBiography(dto.getBiography());
        user.setSecondName(dto.getSecondName());
        user.setFirstName(dto.getFirstName());
        return userRepository.save(user);
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getId(),
                user.get().getPassword(),
                mapRolesToAuthorities(user.get().getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(String id) {
        return userRepository.getById(id).orElseThrow(() -> new NotFoundException("User not found by id"));
    }

    @Override
    public String create(UserRegisterPostRequest dto) {
        return save(dto);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getById(username).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserDetailsService userDetailsService() {
        return this;
    }

    @Override
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    @Override
    public String register(UserRegisterPostRequest dto) {
        String uid = create(dto);
        var user = org.springframework.security.core.userdetails.User.builder()
                .username(uid)
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(Role.ROLE_USER.name())
                .build();
        return jwtService.generateToken(user);
    }

    @Override
    public List<User> findByFilter(String firstName, String lastName) {
        return userRepository.findByFilter(firstName, lastName).stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserById(username);
    }
}
