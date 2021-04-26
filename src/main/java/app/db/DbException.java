package app.db;

public class DbException extends RuntimeException {
    public DbException(String msg, Throwable clause) {
        super(msg, clause);
    }
}
