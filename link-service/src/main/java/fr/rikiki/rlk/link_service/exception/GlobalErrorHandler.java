package fr.rikiki.rlk.link_service.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * Global error handler for the Link Service application.
 * This class handles exceptions thrown by the application and returns appropriate error responses.
 * It uses Spring's @ControllerAdvice to intercept exceptions globally.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@ControllerAdvice
public class GlobalErrorHandler {

    /**
     * Handles validation errors that occur when request parameters or body are not valid.
     *
     * @param ex the exception containing validation errors
     * @param req the HTTP request that caused the exception
     * @return a ResponseEntity containing the validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> onValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ValidationError.FieldError> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ValidationError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        ApiError payload = new ValidationError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed for request",
                req.getRequestURI(),
                fields
        );
        return ResponseEntity.badRequest().body(payload);
    }

    /**
     * Handles LinkNotFoundException when a requested link is not found.
     *
     * @param ex the exception indicating that a link was not found
     * @param req the HTTP request that caused the exception
     * @return a ResponseEntity containing the error details
     */
    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ApiError> onNotFound(LinkNotFoundException ex, HttpServletRequest req) {
        ApiError payload = new NotFoundError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(payload);
    }

    /**
     * Handles generic exceptions that are not specifically handled by other methods.
     * This is a fallback for unexpected errors in the application.
     *
     * @param ex the exception that occurred
     * @param req the HTTP request that caused the exception
     * @return a ResponseEntity containing the error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> onServerError(Exception ex, HttpServletRequest req) {
        ApiError payload = new ServerError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(payload);
    }
}