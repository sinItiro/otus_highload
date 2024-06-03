package com.otus.network.config;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
//@EnableScheduling
public class PGDataSource {
    @Value("${otus.db.db}")
    private String db;
    @Value("${otus.db.user}")
    private String user;
    @Value("${otus.db.password}")
    private String password;
    @Value("${otus.db.host}")
    private String host;
    @Value("${otus.db.port}")
    private String port;

    @Bean
    public DataSource createDataSource() {
        final String url = "jdbc:postgresql://" + host
                + ":" + port
                + "/" + db
                + "?user=" + user
                + "&password=" + password;
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }
}
