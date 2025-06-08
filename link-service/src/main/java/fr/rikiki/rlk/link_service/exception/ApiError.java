package fr.rikiki.rlk.link_service.exception;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * Sealed interface for standardized API error responses.
 * <p>
 * Implementations:
 * <ul>
 *   <li>{@link ValidationError} – for 400 Bad Request validation failures</li>
 *   <li>{@link NotFoundError} – for 404 Not Found errors</li>
 *   <li>{@link ServerError}   – for 500 Internal Server errors</li>
 * </ul>
 * <p>
 * The JSON discriminator field is named "errorType".
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "errorType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ValidationError.class, name = "validation"),
        @JsonSubTypes.Type(value = NotFoundError.class,    name = "not_found"),
        @JsonSubTypes.Type(value = ServerError.class,      name = "server")
})
public sealed interface ApiError
        permits ValidationError, NotFoundError, ServerError {

    /**
     * HTTP status code associated with this error (e.g. 400, 404, 500).
     * @return the HTTP status code
     */
    int getStatus();

    /**
     * Human-readable message describing the error.
     * @return the error message
     */
    String getMessage();

    /**
     * The request path that triggered the error (for diagnostics).
     * @return the request path
     */
    String getPath();

    /**
     * Discriminator value for JSON serialization.
     * Possible values: "validation", "not_found", "server".
     * @return the error type
     */
    String getErrorType();

    /**
     * Specific error code for programmatic handling.
     * Follows a pattern: VAL_XXX (validation), NFD_XXX (not found), SRV_XXX (server).
     * @return the specific error code
     */
    String getErrorCode();
}