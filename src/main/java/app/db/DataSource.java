package app.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {

    private final HikariDataSource ds;

    public DataSource(DbConfig dbConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbConfig.getUrl());
        config.setUsername(dbConfig.getUser());
        config.setPassword(dbConfig.getPass());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {

        return ds.getConnection();
    }

}
