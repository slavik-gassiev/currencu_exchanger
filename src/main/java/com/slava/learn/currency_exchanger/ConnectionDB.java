package com.slava.learn.currency_exchanger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionDB {
    private static final HikariDataSource HIKARI_DATA_SOURCE;

    static {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:sqlite:mydb.db");
        hikariConfig.setDriverClassName("org.sqlite.JDBC");

        HIKARI_DATA_SOURCE = new HikariDataSource(hikariConfig);
    }

    public static Connection getConnection() throws SQLException {
        return HIKARI_DATA_SOURCE.getConnection();
    }
}
