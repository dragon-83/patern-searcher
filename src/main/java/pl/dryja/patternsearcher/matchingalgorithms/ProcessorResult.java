package pl.dryja.patternsearcher.matchingalgorithms;

import com.mongodb.annotations.Immutable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Immutable
@AllArgsConstructor
@ToString
public class ProcessorResult implements Comparable<ProcessorResult> {

    private UUID taskId;
    private int position;
    private int typos;
    private Integer progress;
    private boolean partialOrFullPatternFound;

    public ProcessorResult(final UUID taskId) {
        this.taskId = taskId;
        this.progress = 0;
    }

    public ProcessorResult(final ProcessorResult bestProcessingResult) {
        this.taskId = bestProcessingResult.taskId;
        this.progress = 100;
        this.typos = bestProcessingResult.typos;
        this.partialOrFullPatternFound = bestProcessingResult.partialOrFullPatternFound;
        this.position = bestProcessingResult.position;
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