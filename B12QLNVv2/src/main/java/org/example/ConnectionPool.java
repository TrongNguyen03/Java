package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import javax.sql.DataSource;

public class ConnectionPool {
    private static HikariDataSource dataSource;
    static {
        HikariConfig config=new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/jdbc_test");
        config.setUsername("Trong");
        config.setPassword("31122003");

        config.setMaximumPoolSize(50);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);
        config.setMaxLifetime(1800000);

        dataSource=new HikariDataSource(config);
    }
    public static DataSource getDataSource(){
        return dataSource;
    }
}
