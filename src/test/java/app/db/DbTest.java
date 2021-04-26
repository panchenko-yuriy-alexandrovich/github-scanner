package app.db;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;

public class DbTest {

    DbConfig dbConfig = mock(DbConfig.class);
    DataSource ds;

    @BeforeEach
    void initSystemEnvs() {
        when(dbConfig.getUrl()).thenReturn("jdbc:h2:mem:" + UUID.randomUUID());
        when(dbConfig.getUser()).thenReturn("sa");
        when(dbConfig.getPass()).thenReturn("");
        ds = new DataSource(dbConfig);
    }
}
