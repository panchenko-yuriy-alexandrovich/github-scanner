package app.db;

import static app.db.DbConfig.PASS;
import static app.db.DbConfig.URL;
import static app.db.DbConfig.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.org.webcompere.systemstubs.SystemStubs.withEnvironmentVariable;

import org.junit.jupiter.api.Test;

import app.service.StringUtils;

class DbConfigTest {

    final DbConfig dbConfig = new DbConfig(new StringUtils());

    @Test
    void getUrl() throws Exception {
        String expectedUrl = "jdbc:h2:mem:test";

        withEnvironmentVariable(URL, expectedUrl)
                .execute(() -> assertEquals(expectedUrl, dbConfig.getUrl()));
    }

    @Test
    void getUrl_whenEnvIsEmpty_theReturnLocal() {

        assertEquals("jdbc:postgresql://127.0.0.1:6432/app", dbConfig.getUrl());
    }

    @Test
    void getUser() throws Exception {
        String expectedUser = "user";

        withEnvironmentVariable(USER, expectedUser)
                .execute(() -> assertEquals(expectedUser, dbConfig.getUser()));
    }

    @Test
    void getUser_whenEnvIsEmpty_theReturnDbName() {

        assertEquals(DbConfig.DB_NAME, dbConfig.getUser());
    }

    @Test
    void getPass() throws Exception {
        String expectedPass = "pass";

        withEnvironmentVariable(PASS, expectedPass)
                .execute(() -> assertEquals(expectedPass, dbConfig.getPass()));
    }

    @Test
    void getPass_whenEnvIsEmpty_theReturnDbName() {

        assertEquals(DbConfig.DB_NAME, dbConfig.getPass());
    }
}