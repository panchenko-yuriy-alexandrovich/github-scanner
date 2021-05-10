package app.db;


import app.service.StringUtils;

public class DbConfig {

    public static final String URL = "JDBC_DATABASE_URL";
    public static final String USER = "JDBC_DATABASE_USERNAME";
    public static final String PASS = "JDBC_DATABASE_PASSWORD";
    public static final String DATABASE_URL = "DATABASE_URL";

    public static final String DB_NAME = "app";

    private final StringUtils stringUtils;

    public DbConfig(StringUtils stringUtils) {
        this.stringUtils = stringUtils;
    }

    public String getUrl() {
        String dbUrl = System.getenv(DATABASE_URL);

        if (stringUtils.isEmpty(dbUrl)) {
            String jdbcUrl = System.getenv(URL);

            return stringUtils.isEmpty(jdbcUrl) ? "jdbc:postgresql://127.0.0.1:6432/app" : jdbcUrl;
        } else {
            String[] dbParams = dbUrl.split(":");

            return "jdbc:postgresql://" + dbParams[2].split("@")[1] + ":" + dbParams[3];
        }
    }

    public String getUser() {
        String dbUrl = System.getenv(DATABASE_URL);

        return stringUtils.isEmpty(dbUrl) ?
                (stringUtils.isEmpty(System.getenv(USER)) ? DB_NAME : System.getenv(USER)) :
                dbUrl.split(":")[1].substring(2);
    }

    public String getPass() {
        String dbUrl = System.getenv(DATABASE_URL);

        return stringUtils.isEmpty(dbUrl) ?
                (stringUtils.isEmpty(System.getenv(PASS)) ? DB_NAME : System.getenv(PASS)) :
                dbUrl.split(":")[2].split("@")[0];
    }
}
