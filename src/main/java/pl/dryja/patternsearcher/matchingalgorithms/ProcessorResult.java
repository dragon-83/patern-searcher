package pl.dryja.patternsearcher.matchingalgorithms;

import java.util.UUID;

public record ProcessorResult(UUID taskId, int position, int typos, int progress, boolean partialOrFullPatternFound)
        implements Comparable<ProcessorResult> {

    public ProcessorResult(final UUID taskId) {
        this(taskId, 0, 0, 0, false);
    }

    public ProcessorResult(final ProcessorResult bestProcessingResult) {
        this(bestProcessingResult.taskId, bestProcessingResult.position(), bestProcessingResult.typos, 100,
                bestProcessingResult.partialOrFullPatternFound);
    }

    @Override
    public int compareTo(ProcessorResult o) {

        if (this.typos < o.typos) {
            return 1;
        } else if (this.typos > o.typos) {
            return -1;
        } else {
            return 0;
        }
    }
}