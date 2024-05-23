package com.otus.network.repository.impl;

import com.otus.network.entity.UserEntity;
import com.otus.network.exception.NotExecuteQueryException;
import com.otus.network.exception.NotFoundException;
import com.otus.network.library.api.internal.model.User;
import com.otus.network.repository.Connection;
import com.otus.network.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Connection con;

    @Override
    public Optional<UserEntity> findById(String id) {
        java.sql.Connection connection = con.getConnection();
        String sql = "SELECT * FROM users WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setMaxRows(1);
            ResultSet rs = stmt.executeQuery();
            log.debug(rs.getStatement().toString());
            while (rs.next()) {
                UserEntity user = new UserEntity();
                user.setId(rs.getString("id"));
                user.setBiography(rs.getString("biography"));
                user.setFirstName(rs.getString("firstName"));
                user.setSecondName(rs.getString("secondName"));
                user.setBirthdate(rs.getDate("birthdate").toLocalDate());
                user.setCity(rs.getString("city"));
                user.setPassword(rs.getString("password"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new NotFoundException("User not found", e);
        }
        return Optional.empty();
    }

    @Override
    public String save(UserEntity entity) {
        java.sql.Connection connection = con.getConnection();
        String sql = "INSERT INTO users " +
                " (id, firstname, secondname, birthdate, biography, city, \"password\") " +
                " VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String id = getNewUserId();
            stmt.setString(1, id);
            stmt.setString(2, entity.getFirstName());
            stmt.setString(3, entity.getSecondName());
            stmt.setDate(4, Date.valueOf(entity.getBirthdate()));
            stmt.setString(5, entity.getBiography());
            stmt.setString(6, entity.getCity());
            stmt.setString(7, entity.getPassword());
            int insertedRows = stmt.executeUpdate();
            log.debug(stmt.toString());
            if (insertedRows == 1) {
                return id;
            }
        } catch (SQLException e) {
            throw new NotExecuteQueryException("User not save: " + e.getMessage());
        }

        throw new NotExecuteQueryException("Not get new id for user");
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        java.sql.Connection connection = con.getConnection();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            log.debug(rs.getStatement().toString());
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setBiography(rs.getString("biography"));
                user.setFirstName(rs.getString("firstName"));
                user.setSecondName(rs.getString("secondName"));
                user.setBirthdate(rs.getDate("birthdate").toLocalDate());
                user.setCity(rs.getString("city"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new NotFoundException("User not found", e);
        }
        return users;
    }

    @Override
    public Optional<User> getById(String id) {
        java.sql.Connection connection = con.getConnection();
        String sql = "SELECT * FROM users WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setMaxRows(1);
            ResultSet rs = stmt.executeQuery();
            log.debug(rs.getStatement().toString());
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setBiography(rs.getString("biography"));
                user.setFirstName(rs.getString("firstName"));
                user.setSecondName(rs.getString("secondName"));
                user.setBirthdate(rs.getDate("birthdate").toLocalDate());
                user.setCity(rs.getString("city"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new NotFoundException("User not found", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(String id) {
        return getById(id).isPresent();
    }

    private String getNewUserId() {
        return java.util.UUID.randomUUID().toString();
    }
}
