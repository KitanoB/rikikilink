package fr.rikiki.rlk.link_service.exception;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Sealed interface for standardized API error responses.
 * This interface serves as a polymorphic base for different error types in the API,
 * utilizing JSON type information for proper serialization/deserialization.
 * <p>
 * Three implementations are permitted:
 * <ul>
 *   <li>{@link ValidationError} - For 400 Bad Request errors with field validation details</li>
 *   <li>{@link NotFoundError} - For 404 Not Found errors</li>
 *   <li>{@link ServerError} - For 500 Internal Server errors</li>
 * </ul>
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "errorType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ValidationError.class, name = "validation"),
        @JsonSubTypes.Type(value = NotFoundError.class,    name = "not_found"),
        @JsonSubTypes.Type(value = ServerError.class,      name = "server")
})
public sealed interface ApiError permits ValidationError, NotFoundError, ServerError {

    /**
     * Gets the HTTP status code for this error.
     *
     * @return the HTTP status code as an integer
     */
    int getStatus();

    /**
     * Gets the error message describing what went wrong.
     *
     * @return the error message
     */
    String getMessage();

    /**
     * Gets the request path that resulted in this error.
     *
     * @return the request path
     */
    String getPath();
}


