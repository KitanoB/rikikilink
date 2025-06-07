package fr.rikiki.rlk.link_service.dto;

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
    private String code;
    private String shortUrl;
    private Instant createdAt;

    public LinkResponse(String code, String shortUrl, Instant createdAt) {
        this.code = code;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
    }
}