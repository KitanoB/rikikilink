package fr.rikiki.rlk.link_service.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LeetLikeShortCodeGeneratorTest {

    private List<String> greekWords;
    private List<String> encouragingWords;
    private Random deterministicRandom;
    private int suffixCount;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws Exception {
        // Use small, fixed lists
        greekWords = List.of("zeus", "hera", "ares");
        encouragingWords = List.of("bravo", "cool", "great");
        deterministicRandom = new Random(1234);

        // Reflectively obtain the private LEET_SUFFIXES list size
        Field fld = LeetLikeShortCodeGenerator.class.getDeclaredField("LEET_SUFFIXES");
        fld.setAccessible(true);
        suffixCount = ((List<Character>) fld.get(null)).size();
    }

    @Test
    void shouldThrowWhenGreekListIsEmpty() {
        assertThatThrownBy(() -> new LeetLikeShortCodeGenerator(
                List.of(),
                encouragingWords,
                new Random()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Need at least one word");
    }

    @Test
    void shouldThrowWhenEncouragingListIsEmpty() {
        assertThatThrownBy(() -> new LeetLikeShortCodeGenerator(
                greekWords,
                List.of(),
                new Random()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Need at least one word");
    }

    @Test
    void shouldGenerateCodeWithFormat_GreekDashEncouragingSuffix() {
        var generator = new LeetLikeShortCodeGenerator(greekWords, encouragingWords, deterministicRandom);

        String code = generator.generate();
        // Format: Capitalized Greek + "-" + Capitalized Encouraging + one suffix character
        assertThat(code).matches("^(Zeus|Hera|Ares)-(Bravo|Cool|Great)[4310\\$7]$");
    }

    @Test
    void shouldAlwaysEndWithSingleLeetSuffix() {
        var generator = new LeetLikeShortCodeGenerator(greekWords, encouragingWords, deterministicRandom);

        for (int i = 0; i < 20; i++) {
            String code = generator.generate();
            char lastChar = code.charAt(code.length() - 1);
            assertThat(List.of('4', '3', '1', '0', '$', '7')).contains(lastChar);
        }
    }
}