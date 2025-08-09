package fr.rikiki.rlk.link_service.controller;


import fr.rikiki.rlk.link_service.model.Link;
import fr.rikiki.rlk.link_service.repository.LinkRepository;
import fr.rikiki.rlk.link_service.tracking.ClickPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RedirectController.class)
class RedirectControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LinkRepository linkRepository;

    // if you use Spring Data auditing in your app, stub these out:
    @MockBean
    private JpaMetamodelMappingContext jpaMappingContext;
    @MockBean
    private AuditingHandler jpaAuditingHandler;

    // if you publish click events into Redis or via ApplicationEventPublisher:
    @MockBean
    private ClickPublisher clickPublisher;

    @Test
    void whenCodeExists_then302ToTargetAndPublishEvent() throws Exception {
        // given
        var link = new Link();
        link.setCode("ABC123");
        link.setTargetUrl("https://foo.bar");
        link.setCreatedAt(Instant.now().minus(1, ChronoUnit.DAYS));
        link.setActive(true);

        when(linkRepository.findByCode("ABC123")).thenReturn(Optional.of(link));

        // when / then
        mvc.perform(get("/r/ABC123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://foo.bar"));

        verify(clickPublisher).publishClick("ABC123");
    }

    @Test
    void whenCodeNotFound_then404() throws Exception {
        when(linkRepository.findByCode("NOPPE")).thenReturn(Optional.empty());

        mvc.perform(get("/r/NOPPE"))
                .andExpect(status().isNotFound());
    }
}