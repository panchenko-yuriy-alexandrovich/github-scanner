package app.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

class DataSourceTest extends DbTest {

    @Test
    void getConnection() throws SQLException {
        Connection connection = ds.getConnection();

        assertNotNull(connection);
    }

    @Test
    void migrate() throws SQLException {
        ds.migrate();
        int rowsCount = 0;
        Connection connection = ds.getConnection();
        try (Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery("SELECT  COUNT(*) as size FROM requests")) {
            resultSet.next();
            rowsCount = resultSet.getInt("size");
        }

        assertTrue(rowsCount > 0);
    }

    @Test
    void migrate_whenLiquibaseFail_thenThrowException() {
        Throwable throwable = assertThrows(DbException.class,
                () -> ds.migrate("wrong_name.xml", "test"));

        assertTrue(throwable.getMessage().contains("Liquibase"));
    }
}