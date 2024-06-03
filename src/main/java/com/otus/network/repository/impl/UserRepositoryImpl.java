package com.otus.network.repository.impl;

import com.otus.network.entity.UserEntity;
import com.otus.network.exception.NotExecuteQueryException;
import com.otus.network.exception.NotFoundException;
import com.otus.network.library.api.internal.model.User;
import com.otus.network.repository.Connection;
import com.otus.network.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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
    private PreparedStatement batchStatement;

    @Override
    public Optional<UserEntity> findById(String id) {

        String sql = "SELECT * FROM users WHERE id=?";
        try (java.sql.Connection connection = con.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setMaxRows(1);
            ResultSet rs = stmt.executeQuery();
//            log.debug(rs.getStatement().toString());
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
        String sql = "INSERT INTO users " +
                " (id, firstname, secondname, birthdate, biography, city, \"password\") " +
                " VALUES (?,?,?,?,?,?,?)";
        try (java.sql.Connection connection = con.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            String id = getNewUserId();
            stmt.setString(1, id);
            stmt.setString(2, entity.getFirstName());
            stmt.setString(3, entity.getSecondName());
            stmt.setDate(4, Date.valueOf(entity.getBirthdate()));
            stmt.setString(5, entity.getBiography());
            stmt.setString(6, entity.getCity());
            stmt.setString(7, entity.getPassword());
            int insertedRows = stmt.executeUpdate();
//            log.debug(stmt.toString());
            if (insertedRows == 1) {
                return id;
            }
        } catch (SQLException e) {
            throw new NotExecuteQueryException("User not save: " + e.getMessage());
        }

        throw new NotExecuteQueryException("Not get new id for user");
    }

    @Override
    public void saveBatch(UserEntity entity) {
        String sql = "INSERT INTO users " +
                " (id, firstname, secondname, birthdate, biography, city, \"password\") " +
                " VALUES (?,?,?,?,?,?,?)";
        if (batchStatement == null ) {
            try {
                batchStatement = con.getConnection().prepareStatement(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            String id = getNewUserId();
            batchStatement.setString(1, id);
            batchStatement.setString(2, entity.getFirstName());
            batchStatement.setString(3, entity.getSecondName());
            batchStatement.setDate(4, Date.valueOf(entity.getBirthdate()));
            batchStatement.setString(5, entity.getBiography());
            batchStatement.setString(6, entity.getCity());
            batchStatement.setString(7, entity.getPassword());
            batchStatement.addBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 3000)
    public void scheduleBatchInsert() {
        if (batchStatement == null) {
            log.info("not init batchStatement");
            return;
        }
            try {
                int[] batch = batchStatement.executeBatch();
                log.info("Execute batch: " + batch.length);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (java.sql.Connection connection = con.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
//            log.debug(rs.getStatement().toString());
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
    public List<User> findByFilter(String firstName, String lastName) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE firstName LIKE ? and secondName LIKE ?";
        try (java.sql.Connection connection = con.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, firstName + "%");
            stmt.setString(2, lastName + "%");
            ResultSet rs = stmt.executeQuery();
//            log.debug(rs.getStatement().toString());
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
            throw new NotFoundException("Search user failed", e);
        }
        return users;
    }

    @Override
    public Optional<User> getById(String id) {
        String sql = "SELECT * FROM users WHERE id=?";
        try (java.sql.Connection connection = con.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setMaxRows(1);
            ResultSet rs = stmt.executeQuery();
//            log.debug(rs.getStatement().toString());
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
