package fr.rikiki.rlk.link_service.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Represents a validation error in the API.
 * This class is used to encapsulate details about validation errors,
 * including the status code, message, path, and specific field errors.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@Data
public final class ValidationError implements ApiError {

    /**
     * The HTTP status code for the validation error.
     * This is set to 400, indicating a bad request due to validation issues.
     */
    private int status = 400;

    /**
     * A message describing the validation error.
     * This message provides additional context about the validation issues that occurred.
     */
    private final String message;

    /**
     * The path of the request that caused the validation error.
     * This is useful for debugging and identifying the source of the error.
     */
    private final String path;

    /**
     * A list of field errors that occurred during validation.
     * Each field error contains the name of the field and the specific validation error message.
     */
    private final List<FieldError> fieldErrors;

    /**
     * Constructs a ValidationError with the specified status, message, path, and field errors.
     *
     * @param status      The HTTP status code for the error (should be 400).
     * @param message     A message describing the validation error.
     * @param path        The path of the request that caused the validation error.
     * @param fieldErrors A list of field errors that occurred during validation.
     */
    @JsonCreator
    public ValidationError(
            @JsonProperty("status") int status,
            @JsonProperty("message") String message,
            @JsonProperty("path") String path,
            @JsonProperty("fieldErrors") List<FieldError> fieldErrors
    ) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

    /**
     * Represents a specific field error in the validation process.
     * This record contains the name of the field and the associated error message.
     *
     * @param field The name of the field that has a validation error.
     * @param error The error message describing the validation issue for the field.
     */
    public static record FieldError(
            @JsonProperty("field") String field,
            @JsonProperty("error") String error
    ) {}
}

