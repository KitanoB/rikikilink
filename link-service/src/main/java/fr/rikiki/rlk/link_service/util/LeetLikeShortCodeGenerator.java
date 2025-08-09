package fr.rikiki.rlk.link_service.util;

import org.slf4j.Logger;

import java.util.List;
import java.util.Random;

/**
 * Générateur de codes courts de type "Leet-like".
 * Combine un mot grec et un mot encourageant, suivi d'un suffixe leet.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
public class LeetLikeShortCodeGenerator implements ShortCodeGenerator {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LeetLikeShortCodeGenerator.class);

    /**
     * Suffixes leet possibles.
     * 4, 3, 1, 0, 7
     */
    public static final List<Character> LEET_SUFFIXES = List.of('4', '3', '1', '0', '7');

    /**
     * French words (already filtered to 4–8 letters, a-z).
     */
    private final List<String> frenchWords;

    /**
     * Encouraging words (same criteria: 4–8 letters, a-z).
     */
    private final List<String> encouragingWords;

    /**
     * Random instance for generating random numbers.
     * Can be seeded for testing purposes.
     */
    private final Random random;

    /**
     * @param frenchWords French words filtered to 4–8 letters, a-z
     * @param encouragingWords encouraging words filtered to 4–8 letters, a-z
     * @param random Random instance for generating random numbers.
     */
    public LeetLikeShortCodeGenerator(List<String> frenchWords,
                                      List<String> encouragingWords,
                                      Random random) {
        if (frenchWords.isEmpty() || encouragingWords.isEmpty()) {
            throw new IllegalArgumentException("Need at least one word in each list");
        }
        this.frenchWords = List.copyOf(frenchWords);
        this.encouragingWords = List.copyOf(encouragingWords);
        this.random = random == null ? new Random() : random;
    }

    @Override
    public synchronized String generate() {
        String grec = capitalize(randomItem(frenchWords));
        String encour = capitalize(randomItem(encouragingWords));
        char suffix = LEET_SUFFIXES.get(random.nextInt(LEET_SUFFIXES.size()));
        String result =  grec + "-" + encour + suffix;
        LOGGER.debug("Generated short code: {}", result);
        return result;
    }

    private String randomItem(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}