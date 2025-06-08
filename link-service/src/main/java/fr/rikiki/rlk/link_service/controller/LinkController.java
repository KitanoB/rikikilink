package fr.rikiki.rlk.link_service.controller;

import fr.rikiki.rlk.link_service.dto.LinkCreateRequest;
import fr.rikiki.rlk.link_service.dto.LinkResponse;
import fr.rikiki.rlk.link_service.exception.LinkNotFoundException;
import fr.rikiki.rlk.link_service.model.Link;
import fr.rikiki.rlk.link_service.repository.LinkRepository;
import fr.rikiki.rlk.link_service.util.ShortCodeGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;

/**
 * Controller for managing short links.
 * Provides endpoints to create and retrieve short links.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/links")
public class LinkController {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LinkController.class);

    private final LinkRepository linkRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final String baseUrl;

    public LinkController(LinkRepository linkRepository,
                          ShortCodeGenerator shortCodeGenerator,
                          @Value("${shortlink.base-url:https://riki.li}") String baseUrl) {
        this.linkRepository = linkRepository;
        this.shortCodeGenerator = shortCodeGenerator;
        this.baseUrl = baseUrl;
    }

    @PostMapping
    @Operation(summary = "Create a new short link")
    @ApiResponse(responseCode = "201", description = "Link created successfully")
    public ResponseEntity<LinkResponse> createLink(
            @Valid @RequestBody LinkCreateRequest req
    ) {

        // Validate the request body
        // Link is the entity that represents the data model and that will be saved in the database.
        Link link = new Link();
        link.setTargetUrl(req.getTargetUrl());
        link.setCreatedAt(Instant.now());
        link.setActive(true);

        LOGGER.info("Creating link for target URL: {}", req.getTargetUrl());

        String code = generateUniqueCode();
        link.setCode(code);

        Link saved = linkRepository.save(link);

        String shortUrl = baseUrl + "/" + saved.getCode();
        LinkResponse resp = new LinkResponse(
                saved.getCode(),
                shortUrl,
                saved.getCreatedAt()
        );

        return ResponseEntity
                .created(URI.create("/links/" + saved.getCode()))
                .body(resp);
    }

    /**
     * Generates a unique short code for the link.
     * Ensures that the generated code does not already exist in the repository.
     *
     * @return String a unique short code
     */
    private String generateUniqueCode() {
        String code;

        do {
            code = shortCodeGenerator.generate();
        } while (linkRepository.findByCode(code).isPresent());
        LOGGER.debug("Generated unique code: {}", code);
        return code;
    }

    @GetMapping("/{code}")
    @Operation(summary = "Get a short link by code")
    @ApiResponse(responseCode = "200", description = "Link found")
    public LinkResponse getLink(
            @PathVariable String code
    ) {

        LOGGER.info("Retrieving link for code: {}", code);
        // if the link with the given code does not exist, a LinkNotFoundException will be thrown.
        Link link = linkRepository.findByCode(code)
                .orElseThrow(() -> new LinkNotFoundException(code));

        String shortUrl = baseUrl + "/" + link.getCode();
        return new LinkResponse(
                link.getCode(),
                shortUrl,
                link.getCreatedAt()
        );
    }
}