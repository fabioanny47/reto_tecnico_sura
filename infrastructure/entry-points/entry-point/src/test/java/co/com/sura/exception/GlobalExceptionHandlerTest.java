package co.com.sura.exception;

import co.com.sura.r2dbc.exception.NotFoundException;
import co.com.sura.r2dbc.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleDomain_validationError() {
        ValidationException ex = new ValidationException("VALIDATION_ERROR");

        ResponseEntity<ErrorResponse> response = handler.handleDomain(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDATION_ERROR", response.getBody().code());
        assertEquals("VALIDATION_ERROR", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void handleGeneric() {
        Exception ex = new RuntimeException("Unexpected");

        ResponseEntity<ErrorResponse> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_ERROR", response.getBody().code());
        assertEquals("Unexpected error", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void handleNotFound() {
        NotFoundException ex = new NotFoundException("Resource not found");

        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NOT_FOUND", response.getBody().code());
        assertEquals("Resource not found", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }
}