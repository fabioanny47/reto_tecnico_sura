package co.com.sura.r2dbc.exception;

public class EventPublishingException extends DomainException {
    public EventPublishingException(String message, Throwable cause) {
        super("EMIT_ERROR", message);
    }
}
