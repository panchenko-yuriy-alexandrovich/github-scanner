package app.service;

public class ParseException extends RuntimeException {

    public static final String PATTERN = "Error on parsing string with first 100 symbols [%s] to class %s";

    public ParseException(String msg, Throwable clause) {
        super(msg, clause);
    }
}
