package fr.rikiki.rlk.link_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.rikiki.rlk.link_service.exception.ApiError;
import fr.rikiki.rlk.link_service.exception.NotFoundError;
import fr.rikiki.rlk.link_service.exception.ServerError;
import fr.rikiki.rlk.link_service.exception.ValidationError;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testValidationErrorSerialization() throws JsonProcessingException {
        ValidationError.FieldError fieldError = new ValidationError.FieldError("field1", "must not be null");
        ValidationError validationError = new ValidationError(
                 400,
                "Validation failed",
                "/test/path",
                List.of(fieldError)
        );

        String json = objectMapper.writeValueAsString(validationError);
        assertTrue(json.contains("\"errorType\":\"validation\""));
        assertTrue(json.contains("\"status\":400"));
        assertTrue(json.contains("\"message\":\"Validation failed\""));
        assertTrue(json.contains("\"path\":\"/test/path\""));
        assertTrue(json.contains("\"field\":\"field1\""));
        assertTrue(json.contains("\"error\":\"must not be null\""));
    }

    @Test
    void testValidationErrorDeserialization() throws JsonProcessingException {
        String json = """
                {
                    "errorType": "validation",
                    "status": 400,
                    "message": "Validation failed",
                    "path": "/test/path",
                    "fieldErrors": [
                        {"field": "field1", "error": "must not be null"}
                    ]
                }
                """;

        ApiError apiError = objectMapper.readValue(json, ApiError.class);
        assertTrue(apiError instanceof ValidationError);
        ValidationError validationError = (ValidationError) apiError;
        assertEquals(400, validationError.getStatus());
        assertEquals("Validation failed", validationError.getMessage());
        assertEquals("/test/path", validationError.getPath());
        assertEquals(1, validationError.getFieldErrors().size());
        assertEquals("field1", validationError.getFieldErrors().get(0).field());
        assertEquals("must not be null", validationError.getFieldErrors().get(0).error());
    }

    @Test
    void testNotFoundErrorSerialization() throws JsonProcessingException {
        NotFoundError notFoundError = new NotFoundError(
                404,
                "Resource not found",
                "/test/path"
        );

        String json = objectMapper.writeValueAsString(notFoundError);
        assertTrue(json.contains("\"errorType\":\"not_found\""));
        assertTrue(json.contains("\"status\":404"));
        assertTrue(json.contains("\"message\":\"Resource not found\""));
        assertTrue(json.contains("\"path\":\"/test/path\""));
    }

    @Test
    void testServerErrorSerialization() throws JsonProcessingException {
        ServerError serverError = new ServerError(
                500,
                "Internal server error",
                "/test/path"
        );

        String json = objectMapper.writeValueAsString(serverError);
        assertTrue(json.contains("\"errorType\":\"server\""));
        assertTrue(json.contains("\"status\":500"));
        assertTrue(json.contains("\"message\":\"Internal server error\""));
        assertTrue(json.contains("\"path\":\"/test/path\""));
    }
}
