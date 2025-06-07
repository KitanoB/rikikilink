package fr.rikiki.rlk.link_service.repository;

import fr.rikiki.rlk.link_service.model.Link;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LinkRepositoryTest {

    @Autowired
    private LinkRepository linkRepository;

    @Test
    void should_save_and_retrieve_link_by_code() {
        Link link = new Link();
        link.setCode("abc12345");
        link.setTargetUrl("https://example.com");
        link.setCreatedAt(Instant.now());
        link.setActive(true);

        linkRepository.save(link);

        Optional<Link> found = linkRepository.findByCode("abc12345");

        assertThat(found).isPresent();
        assertThat(found.get().getTargetUrl()).isEqualTo("https://example.com");
    }
}