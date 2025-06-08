package fr.rikiki.rlk.link_service.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * Global exception handler for all controllers in the Link Service.
 * <p>
 * Catches validation failures, resource-not-found, and any other uncaught exceptions,
 * and converts them into our standardized ApiError payloads.
 * </p>
 */
@ControllerAdvice
public class GlobalErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorHandler.class);

    /**
     * Handle all bean‐validation failures (HTTP 400).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        var fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ValidationError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        // Pick the first validation error code based on the underlying constraint
        ValidationErrorCode chosen = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> switch (fe.getCode()) {
                    case "NotBlank", "NotNull"       -> ValidationErrorCode.MISSING_FIELD;
                    case "Pattern", "URL"            -> ValidationErrorCode.INVALID_URL_FORMAT;
                    case "Size"                      -> ValidationErrorCode.SIZE_CONSTRAINT_VIOLATION;
                    case "TypeMismatch", "MethodArgumentTypeMismatchException"
                            -> ValidationErrorCode.TYPE_MISMATCH;
                    default                          -> ValidationErrorCode.BUSINESS_RULE_VIOLATION;
                })
                .findFirst()
                .orElse(ValidationErrorCode.MISSING_FIELD);

        var payload = new ValidationError(
                HttpStatus.BAD_REQUEST.value(),
                chosen.getDefaultMessage(),
                request.getRequestURI(),
                fieldErrors,
                chosen.getCode()
        );

        LOGGER.warn("Validation failed for {}: {}", request.getRequestURI(), fieldErrors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(payload);
    }

    /**
     * Handle when a link code is not found (HTTP 404).
     */
    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            LinkNotFoundException ex,
            HttpServletRequest request
    ) {
        var payload = new NotFoundError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "NFD_001"
        );

        LOGGER.info("Not found at {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(payload);
    }

    /**
     * Catch‐all for any other uncaught exceptions (HTTP 500).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleServerError(
            Exception ex,
            HttpServletRequest request
    ) {
        var payload = new ServerError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                request.getRequestURI(),
                "SRV_001"
        );

        LOGGER.error("Unhandled exception at " + request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(payload);
    }
}