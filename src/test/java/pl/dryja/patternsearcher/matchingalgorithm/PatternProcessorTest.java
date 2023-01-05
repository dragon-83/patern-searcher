package pl.dryja.patternsearcher.matchingalgorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PatternProcessorTest {

    @ParameterizedTest
    @MethodSource("provideInputAndResultForExpectedBehaviorTest")
    void whenProperPatterAndInputAreGivenThenExpectedBehavior(final String input, final String pattern,
                                                              final int position, final int typos,
                                                              final boolean foundPattern) {

        var processor = new PatternProcessor(UUID.randomUUID(), pattern, input);
        processor.processInput();
        var result = processor.getProcessingResult();

        assertThat(result.getTypos()).isEqualTo(typos);
        assertThat(result.getPosition()).isEqualTo(position);
        assertThat(result.isPatternFound()).isEqualTo(foundPattern);
    }

    private static Stream<Arguments> provideInputAndResultForExpectedBehaviorTest() {

        return Stream.of(
                Arguments.of("ABCABC", "CA", 2, 0, true),
                Arguments.of("ABCABC", "ABC", 0, 0, true),
                Arguments.of("ABCDFG", "SDF", 2, 1, true),
                Arguments.of("ABCD", "BCD", 1, 0, true),
                Arguments.of("ABCD", "BWD", 1, 1, true),
                Arguments.of("ABCDEFG", "CFG", 4, 1, true),
                Arguments.of("ABCABC", "ABC", 0, 0, true),
                Arguments.of("ABCDEFG", "TDD", 1, 2, true),
                Arguments.of("ABCDEFG", "QWH", 0, 3, false)
        );
    }

    @Test
    void whenInputIsSmallerThanPatternThenExceptionIsThrow() {

        assertThatThrownBy(() -> {
            var processor = new PatternProcessor(UUID.randomUUID(), "AAAAA", "A");
            processor.processInput();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Input length can't be shorter than pattern length");
    }
}