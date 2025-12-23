package co.com.sura.infrastructure.exception;

public class DatabaseConnectionException extends RuntimeException {
    public DatabaseConnectionException(Throwable cause) {
        super("DB_UNAVAILABLE", cause);
    }
}
 