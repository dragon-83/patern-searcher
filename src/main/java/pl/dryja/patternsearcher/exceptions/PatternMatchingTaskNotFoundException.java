package pl.dryja.patternsearcher.exceptions;

import java.util.UUID;

public class PatternMatchingTaskNotFoundException extends RuntimeException {
    public PatternMatchingTaskNotFoundException(final UUID taskId) {
        super("Not found pattern matching task with the id: " + taskId);
    }
}
