package fr.rikiki.rlk.link_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.Instant;

/**
 * DTO for the response of a link creation request.
 * Contains the code, short URL, and creation timestamp.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@Getter
public class LinkResponse {

    @Schema(description = "The unique code for the shortened link", example = "abc123")
    private String code;

    @Schema(description = "The full short URL", example = "https://rikiki.link/abc123")
    private String shortUrl;

    @Schema(description = "The timestamp when the link was created", example = "2023-10-01T12:00:00Z")
    private Instant createdAt;

    public LinkResponse(String code, String shortUrl, Instant createdAt) {
        this.code = code;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
    }
}