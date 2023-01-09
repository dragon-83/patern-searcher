package pl.dryja.patternsearcher.processingdelegates;

import pl.dryja.patternsearcher.matchingalgorithms.ProcessorResult;

public interface UpdateDelegator {

    void processProgressed(final ProcessorResult processorResult);
}
