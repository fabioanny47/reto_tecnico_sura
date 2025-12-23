package co.com.sura.r2dbc.exception;

public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseConnectionException(Throwable cause) {
        super("DB_UNAVAILABLE", cause);
    }
}
