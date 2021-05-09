package app.db;


import app.service.StringUtils;

public class DbConfig {

    public static final String URL = "DB_URL";
    public static final String USER = "DB_USER";
    public static final String PASS = "DB_PASS";

    public static final String DB_NAME = "app";

    private final StringUtils stringUtils;

    public DbConfig(StringUtils stringUtils) {
        this.stringUtils = stringUtils;
    }

    public String getUrl() {
        String jdbcUrl = System.getenv(URL);

        return stringUtils.isEmpty(jdbcUrl) ? "jdbc:postgresql://127.0.0.1:6432/app" : jdbcUrl;
    }

    public String getUser() {
        String user = System.getenv(USER);

        return stringUtils.isEmpty(user) ? DB_NAME : user;
    }

    public String getPass() {
        String pass = System.getenv(PASS);

        return stringUtils.isEmpty(pass) ? DB_NAME : pass;
    }
}
