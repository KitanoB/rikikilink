package fr.rikiki.rlk.link_service.controller;

import fr.rikiki.rlk.link_service.dto.LinkCreateRequest;
import fr.rikiki.rlk.link_service.dto.LinkResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LinkControllerIntegrationTest {

    @LocalServerPort
    int port;
    @Autowired TestRestTemplate rest;

    @Test
    void fullFlow_persistsAndRetrieves() {
        LinkCreateRequest req = new LinkCreateRequest();
        req.setTargetUrl("https://integration.test/");

        LinkResponse resp = rest.postForObject(
                "http://localhost:" + port + "/links",
                req,
                LinkResponse.class
        );
        assertThat(resp.getCode()).isNotBlank();
        assertThat(resp.getShortUrl())
                .endsWith("/" + resp.getCode());
        assertThat(resp.getCreatedAt()).isNotNull();
    }
}