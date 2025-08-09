package fr.rikiki.rlk.link_service.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration class for the link service.
 * This class defines a bean for grouping OpenAPI endpoints.
 * It matches all paths under the "public" group.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }
}