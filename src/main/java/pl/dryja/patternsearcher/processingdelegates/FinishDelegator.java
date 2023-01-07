package pl.dryja.patternsearcher.processingdelegates;

import java.util.UUID;

public interface FinishDelegator {

    void processFinish(final UUID taskId);
}
