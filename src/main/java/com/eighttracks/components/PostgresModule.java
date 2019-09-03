package com.eighttracks.components;

import org.apache.commons.dbcp2.BasicDataSource;
import org.assertj.core.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostgresModule {
    private final JdbcTemplate db;
    private final NamedParameterJdbcTemplate namedJDBCTemplate;

    @Autowired
    public PostgresModule(@Value("${jdbc.url}") String url,
                           @Value("${jdbc.username}") String username,
                           @Value("${jdbc.password}") String password,
                           @Value("${jdbc.query.timeout.millis}") String timeout,
                           @Value("${jdbc.driver}") String driver,
                           @Value("${jdbc.pool.size}") String poolSize) {
        String jdbcUrl = url + "?user=" + username + "&password=" + password;

        this.db = new JdbcTemplate(createDataSource(jdbcUrl, driver, Integer.parseInt(timeout), Integer.parseInt(poolSize), Optional.empty()));
        this.namedJDBCTemplate = new NamedParameterJdbcTemplate(createDataSource(jdbcUrl, driver, Integer.parseInt(timeout), Integer.parseInt(poolSize), Optional.empty()));
    }

    public JdbcTemplate getDb() {
        return db;
    }

    public NamedParameterJdbcTemplate getNamedJDBCTemplate() {
        return namedJDBCTemplate;
    }

    private static BasicDataSource createDataSource(String url, String driver, int timeout, int poolSize, Optional<Long> timeBetweenEvictionRunsMillis) {
        Preconditions.checkNotNull(driver, "PG driver can't be null");
        Preconditions.checkNotNull(url, "DB driver can't be null");
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setMaxTotal(poolSize);
        dataSource.setMaxWaitMillis((long)timeout);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setDefaultQueryTimeout(timeout);
        timeBetweenEvictionRunsMillis.ifPresent(dataSource::setTimeBetweenEvictionRunsMillis);
        dataSource.setValidationQuery("select 1");
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        return dataSource;
    }
}
