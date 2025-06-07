package fr.rikiki.rlk.link_service.util;

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

    public static final List<Character> LEET_SUFFIXES = List.of('4', '3', '1', '0', '7');
    private final List<String> greekWords;
    private final List<String> encouragingWords;
    private final Random random;

    /**
     * @param greekWords       liste de mots grecs (déjà filtrés 4–8 lettres, a-z)
     * @param encouragingWords liste de mots encourageants (mêmes critères)
     * @param random           Random injecté (peut être seedé en test)
     */
    public LeetLikeShortCodeGenerator(List<String> greekWords,
                                      List<String> encouragingWords,
                                      Random random) {
        if (greekWords.isEmpty() || encouragingWords.isEmpty()) {
            throw new IllegalArgumentException("Need at least one word in each list");
        }
        this.greekWords = List.copyOf(greekWords);
        this.encouragingWords = List.copyOf(encouragingWords);
        this.random = random == null ? new Random() : random;
    }

    @Override
    public synchronized String generate() {
        String grec = capitalize(randomItem(greekWords));
        String encour = capitalize(randomItem(encouragingWords));
        char suffix = LEET_SUFFIXES.get(random.nextInt(LEET_SUFFIXES.size()));
        return grec + "-" + encour + suffix;
    }

    private String randomItem(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}