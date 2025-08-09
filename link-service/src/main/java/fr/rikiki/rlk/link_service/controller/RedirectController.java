package fr.rikiki.rlk.link_service.controller;

import fr.rikiki.rlk.link_service.exception.LinkNotFoundException;
import fr.rikiki.rlk.link_service.model.Link;
import fr.rikiki.rlk.link_service.repository.LinkRepository;
import fr.rikiki.rlk.link_service.tracking.ClickPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Controller for handling redirection requests based on short link codes.
 * <p>
 * Listens on GET "/r/{code}" and:
 *  - returns 302 FOUND with Location header = targetUrl
 *  - or throws LinkNotFoundException (404) if absent/disabled
 *  - and publishes a click event on each hit.
 * </p>
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@Controller
public class RedirectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectController.class);

    private final LinkRepository linkRepository;
    private final ClickPublisher clickPublisher;

    public RedirectController(LinkRepository linkRepository,
                              ClickPublisher clickPublisher) {
        this.linkRepository = linkRepository;
        this.clickPublisher = clickPublisher;
    }

    /**
     * Redirects to the target URL associated with the provided short link code.
     *
     * @param code the short link code
     * @return 302 FOUND with Location header set to the target URL
     * @throws LinkNotFoundException if no active link is found for the code
     */
    @GetMapping("/r/{code}")
    @Operation(summary = "Redirect to the target URL for a given short link code")
    @ApiResponse(responseCode = "302", description = "Redirects to the target URL")
    @ApiResponse(responseCode = "404", description = "Link not found or inactive")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        // lookup + active check
        Link link = linkRepository.findByCode(code)
                .filter(Link::isActive)
                .orElseThrow(() -> new LinkNotFoundException("Link not found: " + code));

        // publish click for analytics
        clickPublisher.publishClick(code);

        LOGGER.info("Redirecting to: {}", link.getTargetUrl());
        return ResponseEntity
                .status(302)
                .location(URI.create(link.getTargetUrl()))
                .build();
    }
}