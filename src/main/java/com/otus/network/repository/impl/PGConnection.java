package com.otus.network.repository.impl;

import com.otus.network.exception.NoConnectionException;
import com.otus.network.repository.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class PGConnection implements Connection {
    private final DataSource dataSource;
    @Override
    public java.sql.Connection getConnection() {
        java.sql.Connection connection;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new NoConnectionException(e);
        }
        return connection;
    }
}
