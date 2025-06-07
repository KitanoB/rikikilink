package fr.rikiki.rlk.link_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.rikiki.rlk.link_service.util.LeetLikeShortCodeGenerator;
import fr.rikiki.rlk.link_service.util.ShortCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Configuration class for the ShortCodeGenerator bean.
 * Loads Greek words and encouraging words from JSON files.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class ShortCodeGeneratorConfig {

    private static final String GREEK_JSON = "/corpora/greek_myths_master.json";
    private static final String ENCOURAGING_JSON = "/corpora/encouraging_words.json";

    @Bean
    public ShortCodeGenerator shortCodeGenerator() throws IOException {
        List<String> greekWords       = loadGreekWords();
        List<String> encouragingWords = loadEncouragingWords();

        return new LeetLikeShortCodeGenerator(
                greekWords,
                encouragingWords,
                new Random()
        );
    }

    private List<String> loadGreekWords() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream(GREEK_JSON)) {
            if (is == null) {
                throw new IOException("Resource not found: " + GREEK_JSON);
            }
            JsonNode root = mapper.readTree(is);
            List<String> all = new ArrayList<>();
            // Pour chaque propriété (greek_gods, greek_titans, …)
            Iterator<String> fieldNames = root.fieldNames();
            while (fieldNames.hasNext()) {
                JsonNode array = root.get(fieldNames.next());
                if (array.isArray()) {
                    for (JsonNode elt : array) {
                        all.add(elt.asText());
                    }
                }
            }
            if (all.isEmpty()) {
                throw new IOException("No Greek words found in " + GREEK_JSON);
            }
            return all;
        }
    }

    private List<String> loadEncouragingWords() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream(ENCOURAGING_JSON)) {
            if (is == null) {
                throw new IOException("Resource not found: " + ENCOURAGING_JSON);
            }
            JsonNode root = mapper.readTree(is).get("encouraging_words");
            if (root == null || !root.isArray()) {
                throw new IOException("Invalid format in " + ENCOURAGING_JSON);
            }
            List<String> words = new ArrayList<>();
            for (JsonNode elt : root) {
                words.add(elt.asText());
            }
            if (words.isEmpty()) {
                throw new IOException("No encouraging words found in " + ENCOURAGING_JSON);
            }
            return words;
        }
    }
}