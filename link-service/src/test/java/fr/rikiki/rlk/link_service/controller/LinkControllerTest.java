package fr.rikiki.rlk.link_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.rikiki.rlk.link_service.dto.LinkCreateRequest;
import fr.rikiki.rlk.link_service.model.Link;
import fr.rikiki.rlk.link_service.repository.LinkRepository;
import fr.rikiki.rlk.link_service.util.ShortCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LinkController.class)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.data.jpa.repositories.enabled=false",
        "spring.jpa.show-sql=false"
})
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LinkRepository linkRepository;

    @MockitoBean
    private ShortCodeGenerator shortcodeGenerator;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;
    @MockitoBean
    private AuditingHandler jpaAuditingHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private LinkCreateRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new LinkCreateRequest();
        validRequest.setTargetUrl("https://example.com/some-page");
    }

    @Test
    void whenValidRequest_andCodeDoesNotExist_thenReturn201AndSavedLink() throws Exception {
        when(shortcodeGenerator.generate()).thenReturn("TestCode1");
        when(linkRepository.findByCode("TestCode1")).thenReturn(Optional.empty());

        ArgumentCaptor<Link> captor = ArgumentCaptor.forClass(Link.class);
        when(linkRepository.save(captor.capture()))
                .thenAnswer(invocation -> {
                    Link l = invocation.getArgument(0);
                    l.setId(UUID.randomUUID());
                    return l;
                });

        mockMvc.perform(post("/links")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.code").value("TestCode1"))
                .andExpect(jsonPath("$.targetUrl").value(validRequest.getTargetUrl()))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());

        verify(shortcodeGenerator).generate();
        verify(linkRepository).findByCode("TestCode1");
        verify(linkRepository).save(any(Link.class));

        Link saved = captor.getValue();
        assertThat(saved.getCode()).isEqualTo("TestCode1");
        assertThat(saved.getTargetUrl()).isEqualTo(validRequest.getTargetUrl());
        assertThat(saved.isActive()).isTrue();
        assertThat(saved.getCreatedAt())
                .isCloseTo(Instant.now(), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void whenGeneratedCodeAlreadyExists_thenTryAgainUntilUnique() throws Exception {
        when(shortcodeGenerator.generate())
                .thenReturn("DupCode", "UniqueCode"); // <-- La logique du mock est correcte ici
        when(linkRepository.findByCode("DupCode"))
                .thenReturn(Optional.of(new Link()));
        when(linkRepository.findByCode("UniqueCode"))
                .thenReturn(Optional.empty());

        ArgumentCaptor<Link> captor = ArgumentCaptor.forClass(Link.class);
        when(linkRepository.save(captor.capture()))
                .thenAnswer(invocation -> {
                    Link l = invocation.getArgument(0);
                    l.setId(UUID.randomUUID());
                    return l;
                });

        mockMvc.perform(post("/links")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("UniqueCode")); // <-- L'assertion est correcte ici

        verify(shortcodeGenerator, times(2)).generate();
        verify(linkRepository).findByCode("DupCode");
        verify(linkRepository).findByCode("UniqueCode");
        verify(linkRepository).save(any(Link.class));
    }

    @Test
    void whenInvalidRequest_thenReturn400() throws Exception {
        mockMvc.perform(post("/links")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(shortcodeGenerator);
        verifyNoInteractions(linkRepository);
    }

    @Test
    void debugMockTest() {
        when(shortcodeGenerator.generate()).thenReturn("MockedCode");
        assertThat(shortcodeGenerator.generate()).isEqualTo("MockedCode");
        verify(shortcodeGenerator).generate();
    }
}