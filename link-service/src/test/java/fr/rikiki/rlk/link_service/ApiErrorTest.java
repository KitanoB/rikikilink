package fr.rikiki.rlk.link_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.rikiki.rlk.link_service.exception.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ApiErrorTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testValidationErrorSerialization() throws JsonProcessingException {
        // use the 3-arg ctor which defaults status=400 and errorCode="VAL_001"
        ValidationError.FieldError fieldError =
                new ValidationError.FieldError("field1", "must not be null");
        ValidationError validationError =
                new ValidationError("Validation failed", "/test/path", List.of(fieldError));

        String json = objectMapper.writeValueAsString(validationError);

        assertTrue(json.contains("\"errorType\":\"validation\""));
        assertTrue(json.contains("\"status\":400"));
        assertTrue(json.contains("\"message\":\"Validation failed\""));
        assertTrue(json.contains("\"path\":\"/test/path\""));
        assertTrue(json.contains("\"field\":\"field1\""));
        assertTrue(json.contains("\"error\":\"must not be null\""));
        // new assertion for errorCode
        assertTrue(json.contains("\"errorCode\":\"VAL_001\""));
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
                    { "field": "field1", "error": "must not be null" }
                  ],
                  "errorCode": "VAL_001"
                }
                """;

        ApiError apiError = objectMapper.readValue(json, ApiError.class);
        assertTrue(apiError instanceof ValidationError);

        ValidationError ve = (ValidationError) apiError;
        assertEquals(400, ve.getStatus());
        assertEquals("Validation failed", ve.getMessage());
        assertEquals("/test/path", ve.getPath());
        assertEquals(1, ve.getFieldErrors().size());
        assertEquals("field1", ve.getFieldErrors().get(0).field());
        assertEquals("must not be null", ve.getFieldErrors().get(0).error());
        assertEquals("VAL_001", ve.getErrorCode());
    }

    @Test
    void testNotFoundErrorSerialization() throws JsonProcessingException {
        // use new 4-arg ctor
        NotFoundError notFoundError =
                new NotFoundError(404, "Resource not found", "/test/path", "NFD_001");

        String json = objectMapper.writeValueAsString(notFoundError);

        assertTrue(json.contains("\"errorType\":\"not_found\""));
        assertTrue(json.contains("\"status\":404"));
        assertTrue(json.contains("\"message\":\"Resource not found\""));
        assertTrue(json.contains("\"path\":\"/test/path\""));
        // new assertion for errorCode
        assertTrue(json.contains("\"errorCode\":\"NFD_001\""));
    }

    @Test
    void testServerErrorSerialization() throws JsonProcessingException {
        // use new 4-arg ctor
        ServerError serverError =
                new ServerError(500, "Internal server error", "/test/path", "SRV_001");

        String json = objectMapper.writeValueAsString(serverError);

        assertTrue(json.contains("\"errorType\":\"server\""));
        assertTrue(json.contains("\"status\":500"));
        assertTrue(json.contains("\"message\":\"Internal server error\""));
        assertTrue(json.contains("\"path\":\"/test/path\""));
        // new assertion for errorCode
        assertTrue(json.contains("\"errorCode\":\"SRV_001\""));
    }
}