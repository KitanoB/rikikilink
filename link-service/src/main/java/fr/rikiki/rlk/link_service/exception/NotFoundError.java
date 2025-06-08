package fr.rikiki.rlk.link_service.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents a 404 Not Found error in the API.
 * This error is used when a requested resource cannot be found.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@Data
public final class NotFoundError implements ApiError {

    /**
     * The HTTP status code for the Not Found error.
     * This is set to 404, indicating that the requested resource could not be found.
     */
    private int status = 404;

    /**
     * A message describing the Not Found error.
     * This message provides additional context about the error that occurred.
     */
    private final String message;

    /**
     * The path of the request that caused the Not Found error.
     * This is useful for debugging and identifying the source of the error.
     */
    private final String path;

    /**
     * The error code that specifically identifies this not found error.
     * Format: NFD_XXX
     */
    private final String errorCode;

    /**
     * Constructs a NotFoundError with the specified status, message, and path.
     *
     * @param status    The HTTP status code for the error (should be 404).
     * @param message   A message describing the error.
     * @param path      The path of the request that caused the error.
     * @param errorCode The specific error code for this not found error.
     */
    @JsonCreator
    public NotFoundError(
            @JsonProperty("status") int status,
            @JsonProperty("message") String message,
            @JsonProperty("path") String path,
            @JsonProperty("errorCode") String errorCode
    ) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.errorCode = errorCode != null ? errorCode : "NFD_001";
    }

    /**
     * Constructs a NotFoundError with the default status (404), specified message, and path.
     *
     * @param message   A message describing the error.
     * @param path      The path of the request that caused the error.
     * @param errorCode The specific error code for this not found error.
     */
    public NotFoundError(String message, String path, String errorCode) {
        this(404, message, path, errorCode);
    }

    /**
     * Constructs a NotFoundError with the default status (404), specified message, path,
     * and default error code (NFD_001).
     *
     * @param message A message describing the error.
     * @param path    The path of the request that caused the error.
     */
    public NotFoundError(String message, String path) {
        this(404, message, path, "NFD_001");
    }

    @Override
    public String getErrorType() {
        return "not_found";
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}