package fr.rikiki.rlk.link_service.exception;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a server error response.
 * This class is used to encapsulate details about server errors that occur during the processing of requests.
 * It includes the HTTP status code, a message describing the error, and the path of the request that caused the error.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@Data
public final class ServerError implements ApiError {

    /**
     * The HTTP status code for the server error.
     * This is set to 500, indicating an internal server error.
     */
    private int status = 500;

    /**
     * A message describing the server error.
     * This message provides additional context about the error that occurred.
     */
    private final String message;

    /**
     * The path of the request that caused the server error.
     * This is useful for debugging and identifying the source of the error.
     */
    private final String path;

    @JsonCreator
    public ServerError(
            @JsonProperty("status") int status,
            @JsonProperty("message") String message,
            @JsonProperty("path") String path
    ) {
        this.status = status;
        this.message = message;
        this.path = path;
    }
}