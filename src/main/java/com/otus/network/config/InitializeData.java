package com.otus.network.config;

import com.otus.network.entity.UserEntity;
import com.otus.network.exception.NotExecuteQueryException;
import com.otus.network.exception.ValidateException;
import com.otus.network.repository.Connection;
import com.otus.network.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;


@Component
@RequiredArgsConstructor
@Slf4j
public class InitializeData {
    private final Connection con;
    private final UserRepository userRepository;

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

        boolean needInsertData = false;
        if(!needInsertData) return;

        log.info("Start load users from .csv");
        File file = new File("src/main/resources/people.v2.csv");
        try {
            AtomicInteger count = new AtomicInteger(0);
            Files.lines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8)
                    .limit(999_900)
                    .map(InitializeData::mapStringToUser)
                    .peek((__) -> count.incrementAndGet())
                    .forEach(userRepository::saveBatch);
            log.info("Загружено пользователей: " + count.get());
        } catch (IOException e) {
            throw new ValidateException("User not loaded from .csv: " + e.getMessage());
        }
    }

    private static UserEntity mapStringToUser(String str) {
        String[] split = str.split(",");
        if (split.length != 3) {
            throw new ValidateException("Ошибка конвертации пользователя: " + str);
        }
        String[] fi = split[0].split(" ");
        if (fi.length != 2) {
            throw new ValidateException("Ошибка валидации Фамилии и Имени" + str);
        }
        LocalDate localDate = LocalDate.parse(split[1]);
        return UserEntity.builder()
                .firstName(fi[0])
                .secondName(fi[1])
                .birthdate(localDate)
                .city(split[2])
                .build();
    }
}
