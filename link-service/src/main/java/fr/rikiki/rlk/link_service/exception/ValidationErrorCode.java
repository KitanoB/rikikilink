package fr.rikiki.rlk.link_service.exception;

/**
 * Enum representing validation error codes and their default messages.
 * This enum is used to categorize and provide meaningful messages for validation errors
 * that may occur during the processing of requests in the link service.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
public enum ValidationErrorCode {

    MISSING_FIELD("VAL_001", "Required field is missing"),
    INVALID_URL_FORMAT("VAL_002", "URL format is invalid"),
    SIZE_CONSTRAINT_VIOLATION("VAL_003", "Field size out of bounds"),
    TYPE_MISMATCH("VAL_004", "Request type mismatch"),
    BUSINESS_RULE_VIOLATION("VAL_005", "Business rule violation");

    private final String code;
    private final String defaultMessage;

    ValidationErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }


    public String getCode() {
        return code;
    }


    public String getDefaultMessage() {
        return defaultMessage;
    }
}
