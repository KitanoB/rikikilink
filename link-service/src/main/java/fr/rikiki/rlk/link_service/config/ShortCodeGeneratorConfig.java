package fr.rikiki.rlk.link_service.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.rikiki.rlk.link_service.util.LeetLikeShortCodeGenerator;
import fr.rikiki.rlk.link_service.util.ShortCodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Configuration class for the ShortCodeGenerator bean.
 * Loads French words and encouraging words from JSON files.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class ShortCodeGeneratorConfig {

    private final ResourceLoader resourceLoader;
    private final String frenchJsonPath;
    private final String encouragingJsonPath;

    public ShortCodeGeneratorConfig(
            ResourceLoader resourceLoader,
            @Value("${corpora.french.words}") String frenchWords,
            @Value("${corpora.encouraging.words}") String encouragingJsonPath) {
        this.resourceLoader = resourceLoader;
        this.frenchJsonPath = frenchWords;
        this.encouragingJsonPath = encouragingJsonPath;
    }

    @Bean
    public ShortCodeGenerator shortCodeGenerator() throws IOException {
        List<String> frenchWords = loadFrenchWords();
        List<String> encouragingWords = loadEncouragingWords();

        return new LeetLikeShortCodeGenerator(
                frenchWords,
                encouragingWords,
                new Random()
        );
    }

    private List<String> loadFrenchWords() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Resource resource = resourceLoader.getResource("classpath:" + frenchJsonPath);

        if (!resource.exists()) {
            resource = resourceLoader.getResource("file:" + frenchJsonPath);
        }

        try (InputStream is = resource.getInputStream()) {
            JsonNode root = mapper.readTree(is);
            List<String> all = new ArrayList<>();
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
                throw new IOException("No French words found in " + frenchJsonPath);
            }
            return all;
        }
    }

    private List<String> loadEncouragingWords() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Resource resource = resourceLoader.getResource("classpath:" + encouragingJsonPath);

        if (!resource.exists()) {
            resource = resourceLoader.getResource("file:" + encouragingJsonPath);
        }

        try (InputStream is = resource.getInputStream()) {
            JsonNode root = mapper.readTree(is).get("encouraging_words");
            if (root == null || !root.isArray()) {
                throw new IOException("Invalid format in " + encouragingJsonPath);
            }
            List<String> words = new ArrayList<>();
            for (JsonNode elt : root) {
                words.add(elt.asText());
            }
            if (words.isEmpty()) {
                throw new IOException("No encouraging words found in " + encouragingJsonPath);
            }
            return words;
        }
    }
}