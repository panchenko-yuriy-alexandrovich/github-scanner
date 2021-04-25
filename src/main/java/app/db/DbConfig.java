package app.db;

public class DbConfig {

    public static final String URL = "DB_URL";
    public static final String USER = "DB_USER";
    public static final String PASS = "DB_PASS";

    public String getUrl() {

        return System.getenv(URL);
    }

    public String getUser() {

        return System.getenv(USER);
    }

    public String getPass() {

        return System.getenv(PASS);
    }
}
