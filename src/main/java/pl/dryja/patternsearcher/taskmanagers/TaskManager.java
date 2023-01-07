package pl.dryja.patternsearcher.taskmanagers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.dryja.patternsearcher.matchingalgorithms.PatternProcessor;
import pl.dryja.patternsearcher.matchingalgorithms.ProcessingProgress;
import pl.dryja.patternsearcher.processingdelegates.FinishDelegator;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

@Component
@AllArgsConstructor
@Slf4j
public class TaskManager implements FinishDelegator {

    private final Executor executor;
    private final ConcurrentHashMap<UUID, PatternProcessor> processingMap = new ConcurrentHashMap<>();

    public void runTask(final PatternProcessor processor) {

        log.info("Starting matching process for task id: {}", processor.getTaskId());
        processor.addFinishDelegator(this);
        processingMap.put(processor.getTaskId(), processor);
        executor.execute(
            () -> processor.processInput()
        );
    }

    public ProcessingProgress getPercentageProgress(final UUID taskId) {

        log.info("Search task in running processes: {}", taskId);
        if (processingMap.containsKey(taskId)) {
            return processingMap.get(taskId);
        }

        log.info("Search task in persisted processes: {}", taskId);
        //TODO implement check and return task from repo
        return null;
    }

    @Override
    public void processFinish(final UUID taskId) {

        log.info("Finished task process: {}", taskId);
        final var patternProcessor = processingMap.get(taskId);
        //TODO implement save result in repo

        final var processingResult = patternProcessor.getProcessingResult();
        log.info("Success for task with id {}, typos: {}, position {} !!!", taskId, processingResult.getTypos(),
                processingResult.getPosition());

        processingMap.remove(taskId);
    }
}
