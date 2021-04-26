package app.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

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
        config.setAutoCommit(true);
        ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {

        return ds.getConnection();
    }

    public void migrate() {
        migrate("db.xml", "db");
    }

    void migrate(String fileName, String context) {
        Connection conn = null;
        try {
            conn = getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase(fileName, new ClassLoaderResourceAccessor(), database);
            liquibase.update(context);
        } catch (SQLException | LiquibaseException e) {
            throw new DbException("Error on migrating Liquibase: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.rollback();
                    conn.close();
                } catch (SQLException e) {
                    //nothing to do
                }
            }
        }
    }

}
