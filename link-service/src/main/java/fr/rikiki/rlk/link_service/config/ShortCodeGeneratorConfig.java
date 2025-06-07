package fr.rikiki.rlk.link_service.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.rikiki.rlk.link_service.util.LeetLikeShortCodeGenerator;
import fr.rikiki.rlk.link_service.util.ShortCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

@Configuration
public class ShortCodeGeneratorConfig {

    @Bean
    public ShortCodeGenerator shortCodeGenerator() throws IOException {
        List<String> greekWords = loadGreekWords();
        List<String> encouragingWords = loadEncouragingWords();

        return new LeetLikeShortCodeGenerator(
                greekWords,
                encouragingWords,
                new Random()
        );
    }

    private List<String> loadGreekWords() throws IOException {
        // load greek words from a resource greek_myths_master.json
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getResourceAsStream("/greek_myths_master.json")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: greek_myths_master.json");
            }
            return objectMapper.readValue(inputStream, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            throw new IOException("Failed to load Greek words", e);
        }
    }

    private List<String> loadEncouragingWords() throws IOException {
        // load encouraging words from a resource encouraging_words.json
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getResourceAsStream("/encouraging_words.json")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: encouraging_words.json");
            }
            return objectMapper.readValue(inputStream, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            throw new IOException("Failed to load encouraging words", e);
        }
    }
}