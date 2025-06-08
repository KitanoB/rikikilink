package fr.rikiki.rlk.link_service.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
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
     * Constructs a NotFoundError with the specified status, message, and path.
     *
     * @param status  The HTTP status code for the error (should be 404).
     * @param message A message describing the error.
     * @param path    The path of the request that caused the error.
     */
    @JsonCreator
    public NotFoundError(
            @JsonProperty("status") int status,
            @JsonProperty("message") String message,
            @JsonProperty("path") String path
    ) {
        this.status = status;
        this.message = message;
        this.path = path;
    }
}