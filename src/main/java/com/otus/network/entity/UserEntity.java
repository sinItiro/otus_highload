package com.otus.network.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    protected String id;
    protected String firstName;
    protected String secondName;
    protected LocalDate birthdate;
    protected String biography;
    protected String city;
    protected String password;
    public Collection<Role> getRoles() {
        return List.of(Role.ROLE_USER);
    }
}
