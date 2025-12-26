package co.com.sura.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard error response")
public record ErrorResponse(
        @Schema(example = "NOT_FOUND") String code,
        @Schema(example = "User not found") String message,
        @Schema(example = "2025-12-22T12:00:00") LocalDateTime timestamp
) {}
