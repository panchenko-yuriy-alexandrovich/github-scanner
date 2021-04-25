package app.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DataSourceTest {

    static DbConfig dbConfig = mock(DbConfig.class);

    @BeforeAll
    static void initSystemEnvs() {
        when(dbConfig.getUrl()).thenReturn("jdbc:h2:mem:app");
        when(dbConfig.getUser()).thenReturn("sa");
        when(dbConfig.getPass()).thenReturn("");
    }

    @Test
    void getConnection() throws SQLException {
        DataSource ds = new DataSource(dbConfig);
        Connection connection = ds.getConnection();

        assertNotNull(connection);
    }
}