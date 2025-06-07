package fr.rikiki.rlk.link_service.controller;

import fr.rikiki.rlk.link_service.dto.LinkCreateRequest;
import fr.rikiki.rlk.link_service.dto.LinkResponse;
import fr.rikiki.rlk.link_service.model.Link;
import fr.rikiki.rlk.link_service.repository.LinkRepository;
import fr.rikiki.rlk.link_service.util.ShortCodeGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    private final LinkRepository repo;
    private final ShortCodeGenerator gen;
    private final String baseUrl = "https://riki.li";

    public LinkController(LinkRepository repo, ShortCodeGenerator gen) {
        this.repo = repo;
        this.gen  = gen;
    }

    @PostMapping
    @Operation(summary = "Create a new short link")
    @ApiResponse(responseCode = "201", description = "Link created successfully")
    public ResponseEntity<LinkResponse> createLink(
            @Valid @RequestBody LinkCreateRequest req
    ) {

        Link link = new Link();
        link.setTargetUrl(req.getTargetUrl());
        link.setCreatedAt(java.time.Instant.now());
        link.setActive(true);

        String code;
        do {
            code = gen.generate();
        } while (repo.findByCode(code).isPresent());
        link.setCode(code);

        Link saved = repo.save(link);

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

    @GetMapping("/{code}")
    @Operation(summary = "Get a short link by code")
    @ApiResponse(responseCode = "200", description = "Link found")
    public ResponseEntity<LinkResponse> getLink(
            @PathVariable String code
    ) {
        return repo.findByCode(code)
                .map(link -> {
                    String shortUrl = baseUrl + "/" + link.getCode();
                    LinkResponse resp = new LinkResponse(
                            link.getCode(),
                            shortUrl,
                            link.getCreatedAt()
                    );
                    return ResponseEntity.ok(resp);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}