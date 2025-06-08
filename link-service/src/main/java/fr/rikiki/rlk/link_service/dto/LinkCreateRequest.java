package fr.rikiki.rlk.link_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for creating a new link.
 * Contains validation constraints to ensure the target URL is not blank and is a valid HTTP/HTTPS URL.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@Data
public class LinkCreateRequest {

    @NotBlank(message = "targetUrl must not be blank")
    @Pattern(
            regexp = "https?://.+",
            message = "targetUrl must be a valid HTTP/HTTPS URL"
    )
    @Schema(description="The URL to shorten", example="https://example.com/foo")
    private String targetUrl;
}