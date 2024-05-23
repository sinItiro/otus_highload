package com.otus.network.config;

import com.otus.network.exception.NotExecuteQueryException;
import com.otus.network.repository.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.Statement;


@Component
@RequiredArgsConstructor
@Slf4j
public class InitializeData {
    private final Connection con;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Start init");
        String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
                "\tid varchar NOT NULL,\n" +
                "\tfirstname varchar NULL,\n" +
                "\tsecondname varchar NULL,\n" +
                "\tbirthdate date NULL,\n" +
                "\tbiography text NULL,\n" +
                "\tcity varchar NULL,\n" +
                "\t\"password\" varchar NULL,\n" +
                "\tCONSTRAINT pk PRIMARY KEY (id)\n" +
                ");";

        try (java.sql.Connection connection = con.getConnection();) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            log.debug(statement.toString());
        } catch (SQLException e) {
            throw new NotExecuteQueryException("Table users not create: " + e.getMessage());
        }
        log.info("Finish init");
    }
}
