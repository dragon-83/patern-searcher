package pl.dryja.patternsearcher.matchingalgorithms;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.dryja.patternsearcher.processingdelegates.FinishActionDelegate;
import pl.dryja.patternsearcher.processingdelegates.FinishDelegator;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class PatternProcessor implements ProcessingProgress, FinishActionDelegate {

    private final AtomicInteger inputPointerPosition = new AtomicInteger();

    @Getter
    private final UUID taskId;

    private final int inputLength;
    private final int patternLength;

    private final String[] pattern;
    private final String[] input;

    private FinishDelegator delegate;

    @Getter
    private ProcessorResult processingResult;

    public PatternProcessor(final UUID taskId, final String pattern, final String input) {

        this.taskId = taskId;

        this.inputLength = input.length();
        this.patternLength = pattern.length();

        this.pattern = pattern.split("");
        this.input = input.split("");

        this.inputPointerPosition.set(0);

        validate();
    }

    public void processInput() {

        for (int inputPointer = 0; inputPointer < this.inputLength; inputPointer++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            inputPointerPosition.set(inputPointer);
            if (processPatternOnInput(inputPointer)) break;
        }
        inputPointerPosition.set(inputLength);

        delegate.processFinish(this.taskId);
        delegate = null;
    }

    @Override
    public int progressInPercentage() {

        return (int)(((inputPointerPosition.get() / (float)inputLength)) * 100);
    }

    @Override
    public void addFinishDelegator(FinishDelegator finishDelegator) {

        this.delegate = finishDelegator;
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
        final var result = new ProcessorResult(inputPointer, typos, foundPattern);
        if (Objects.isNull(this.processingResult)) {
            this.processingResult = result;
        } else if (this.processingResult.compareTo(result) < 0) {
            this.processingResult = result;
        }
    }

    private boolean isPatternFound(final int typos) {

        return this.patternLength != typos;
    }
}