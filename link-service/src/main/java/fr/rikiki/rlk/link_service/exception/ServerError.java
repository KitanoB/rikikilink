package fr.rikiki.rlk.link_service.exception;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    /**
     * The error code that specifically identifies this server error.
     * Format: SRV_XXX
     */
    private final String errorCode;

    /**
     * Constructs a ServerError with the specified status, message, path, and error code.
     *
     * @param status    The HTTP status code for the error (should be 500).
     * @param message   A message describing the error.
     * @param path      The path of the request that caused the error.
     * @param errorCode The specific error code for this server error.
     */
    @JsonCreator
    public ServerError(
            @JsonProperty("status") int status,
            @JsonProperty("message") String message,
            @JsonProperty("path") String path,
            @JsonProperty("errorCode") String errorCode
    ) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.errorCode = errorCode != null ? errorCode : "SRV_001";
    }

    /**
     * Constructs a ServerError with the default status (500), specified message, and path.
     *
     * @param message   A message describing the error.
     * @param path      The path of the request that caused the error.
     * @param errorCode The specific error code for this server error.
     */
    public ServerError(String message, String path, String errorCode) {
        this(500, message, path, errorCode);
    }

    /**
     * Constructs a ServerError with the default status (500), specified message, path,
     * and default error code (SRV_001).
     *
     * @param message A message describing the error.
     * @param path    The path of the request that caused the error.
     */
    public ServerError(String message, String path) {
        this(500, message, path, "SRV_001");
    }

    @Override
    public String getErrorType() {
        return "server";
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}