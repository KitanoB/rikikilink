package fr.rikiki.rlk.link_service.controller;

import fr.rikiki.rlk.link_service.dto.LinkCreateRequest;
import fr.rikiki.rlk.link_service.model.Link;
import fr.rikiki.rlk.link_service.repository.LinkRepository;
import fr.rikiki.rlk.link_service.util.ShortCodeGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/links")
public class LinkController {

    private final LinkRepository linkRepository;
    private final ShortCodeGenerator shortcodeGenerator;

    @Autowired
    public LinkController(LinkRepository linkRepository, ShortCodeGenerator shortcodeGenerator) {
        this.linkRepository = linkRepository;
        this.shortcodeGenerator = shortcodeGenerator;
    }

    @PostMapping
    public ResponseEntity<Link> createLink(@Valid @RequestBody LinkCreateRequest request) {
        Link link = new Link();
        link.setTargetUrl(request.getTargetUrl());
        link.setCreatedAt(Instant.now());
        link.setActive(true);

        String code;
        do {
            code = shortcodeGenerator.generate();
        } while (linkRepository.findByCode(code).isPresent());
        link.setCode(code);

        Link saved = linkRepository.save(link);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}