package fr.rikiki.rlk.link_service.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiController {

    @Value("classpath:static/openapi.yaml")
    private Resource openapiResource;

    @GetMapping(value = "/openapi.yaml", produces = "application/yaml")
    public ResponseEntity<Resource> getOpenApiSpec() {
        return ResponseEntity.ok().body(openapiResource);
    }
}
