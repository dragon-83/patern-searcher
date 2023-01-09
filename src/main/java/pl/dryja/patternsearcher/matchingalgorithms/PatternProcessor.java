package pl.dryja.patternsearcher.matchingalgorithms;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.dryja.patternsearcher.processingdelegates.UpdateActionDelegate;
import pl.dryja.patternsearcher.processingdelegates.UpdateDelegator;

import java.util.Objects;
import java.util.UUID;

@Slf4j
public class PatternProcessor implements UpdateActionDelegate {

    private Integer inputPointerPosition;

    @Getter
    private final UUID taskId;

    private final int inputLength;
    private final int patternLength;

    private final String[] pattern;
    private final String[] input;

    private UpdateDelegator delegate;

    @Getter
    private ProcessorResult bestProcessingResult;

    public PatternProcessor(final UUID taskId, final String pattern, final String input) {

        this.taskId = taskId;

        this.inputLength = input.length();
        this.patternLength = pattern.length();

        this.pattern = pattern.split("");
        this.input = input.split("");

        this.inputPointerPosition = 0;

        validate();
    }

    public void processInput() {

        for (int inputPointer = 0; inputPointer < this.inputLength; inputPointer++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            inputPointerPosition = inputPointer;
            if (processPatternOnInput(inputPointer)) break;
        }
        inputPointerPosition = inputLength;

        delegate.processProgressed(new ProcessorResult(this.bestProcessingResult));
        delegate = null;
    }

    private int progressInPercentage() {

        return (int)(((inputPointerPosition / (float)inputLength)) * 100);
    }

    @Override
    public void addUpdateDelegator(UpdateDelegator updateDelegator) {

        this.delegate = updateDelegator;
    }

    private void validate() {

        if (this.inputLength < this.patternLength) {
            throw new IllegalArgumentException("Input length can't be shorter than pattern length");
        }
    }

    private boolean processPatternOnInput(int inputPointer) {

        var typos = 0;
        for (int patternPointer = 0; patternPointer < this.patternLength; patternPointer++) {
            var internalInputPointer = inputPointer + patternPointer;
            if (internalInputPointer >= this.inputLength) {
                return true;
            }
            if (!this.input[internalInputPointer].equals(this.pattern[patternPointer])) {
                typos++;
            }
        }

        assignResultIfApplicable(inputPointer, typos);
        return false;
    }

    private void assignResultIfApplicable(int inputPointer, int typos) {

        final var foundPattern = isPatternFound(typos);
        final var progressPercentage = progressInPercentage();
        final var result = new ProcessorResult(taskId, inputPointer, typos, progressPercentage, foundPattern);
        delegate.processProgressed(result);
        if (Objects.isNull(this.bestProcessingResult)) {
            this.bestProcessingResult = result;
        } else if (this.bestProcessingResult.compareTo(result) < 0) {
            this.bestProcessingResult = result;
        }
    }

    private boolean isPatternFound(final int typos) {

        return this.patternLength != typos;
    }
}