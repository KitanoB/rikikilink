package fr.rikiki.rlk.link_service.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LeetLikeShortCodeGeneratorTest {

    private List<String> frenchWords;
    private List<String> encouragingWords;
    private Random deterministicRandom;
    private int suffixCount;

    @BeforeEach
    void setUp() throws Exception {
        // Use small, fixed lists
        frenchWords = List.of("baguette", "ouioui", "pigalle");
        encouragingWords = List.of("bravo", "cool", "super");
        deterministicRandom = new Random(1234);

        // Reflectively obtain the private LEET_SUFFIXES list size
        Field fld = LeetLikeShortCodeGenerator.class.getDeclaredField("LEET_SUFFIXES");
        fld.setAccessible(true);
        suffixCount = ((List<?>) fld.get(null)).size();
    }

    @Test
    void shouldThrowWhenFrenchListIsEmpty() {
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
                frenchWords,
                List.of(),
                new Random()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Need at least one word");
    }

    @Test
    void shouldGenerateCodeWithFormat_FrenchDashEncouragingSuffix() {
        var generator = new LeetLikeShortCodeGenerator(frenchWords, encouragingWords, deterministicRandom);

        String code = generator.generate();
        // Format: Capitalized French + "-" + Capitalized Encouraging + one suffix character
        assertThat(code).matches("^(Baguette|OuiOui|Pigalle)-(Bravo|Cool|Super)[4310\\$7]$");
    }

    @Test
    void shouldAlwaysEndWithSingleLeetSuffix() {
        var generator = new LeetLikeShortCodeGenerator(frenchWords, encouragingWords, deterministicRandom);

        for (int i = 0; i < 20; i++) {
            String code = generator.generate();
            char lastChar = code.charAt(code.length() - 1);
            assertThat(List.of('4', '3', '1', '0', '$', '7')).contains(lastChar);
        }
    }
}