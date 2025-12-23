package co.com.sura.r2dbc.exception;

public class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}