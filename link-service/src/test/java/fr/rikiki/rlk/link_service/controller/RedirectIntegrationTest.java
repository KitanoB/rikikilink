package fr.rikiki.rlk.link_service.controller;

import fr.rikiki.rlk.link_service.model.Link;
import fr.rikiki.rlk.link_service.repository.LinkRepository;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RedirectIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private LinkRepository linkRepository;

    @Test
    void whenCodeExists_thenRedirects302() {
        // Given
        Link link = new Link();
        link.setCode("ABC");
        link.setTargetUrl("https://example.com");
        link.setCreatedAt(Instant.now());
        link.setActive(true);

        given(linkRepository.findByCode("ABC"))
                .willReturn(Optional.of(link));

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(HttpClients.custom()
                .disableRedirectHandling()
                .build());
        restTemplate.getRestTemplate().setRequestFactory(factory);

        // When
        ResponseEntity<String> response = restTemplate.getForEntity("/r/ABC", String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND); // 302 FOUND = MOVED_TEMPORARILY
        assertThat(response.getHeaders().getLocation())
                .hasToString("https://example.com");
    }

    @Test
    void whenCodeMissing_thenReturns404() {
        // Given
        given(linkRepository.findByCode("NOPPE"))
                .willReturn(Optional.empty());

        // When
        ResponseEntity<String> response = restTemplate.getForEntity("/r/NOPPE", String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenLinkDisabled_thenReturns404() {
        // Given
        Link link = new Link();
        link.setCode("DIS");
        link.setTargetUrl("https://example.org");
        link.setCreatedAt(Instant.now());
        link.setActive(false);

        given(linkRepository.findByCode("DIS"))
                .willReturn(Optional.of(link));

        // When
        ResponseEntity<String> response = restTemplate.getForEntity("/r/DIS", String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}