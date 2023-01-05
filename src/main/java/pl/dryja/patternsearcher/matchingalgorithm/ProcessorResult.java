package pl.dryja.patternsearcher.matchingalgorithm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessorResult implements Comparable<ProcessorResult> {

    private int position;
    private int typos;
    private boolean patternFound;

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