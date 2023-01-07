package pl.dryja.patternsearcher.matchingalgorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessorResultTest {

    @ParameterizedTest
    @MethodSource("provideProcessorResultsToCompareTest")
    void whenComparingProcessorResultsThenExpectedBehavior(final ProcessorResult first, final ProcessorResult second,
                                                           final int result) {

        assertThat(first.compareTo(second)).isEqualTo(result);
    }

    private static Stream<Arguments> provideProcessorResultsToCompareTest() {

        return Stream.of(
                Arguments.of(new ProcessorResult(0, 4, false),
                        new ProcessorResult(0, 4, false), 0),
                Arguments.of(new ProcessorResult(0, 5, false),
                        new ProcessorResult(0, 4, false), -1),
                Arguments.of(new ProcessorResult(0, 4, false),
                        new ProcessorResult(0, 5, false), 1)
        );
    }
}