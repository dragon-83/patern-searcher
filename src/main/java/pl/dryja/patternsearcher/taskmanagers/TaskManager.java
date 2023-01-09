package pl.dryja.patternsearcher.taskmanagers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.dryja.patternsearcher.exceptions.PatternMatchingTaskNotFoundException;
import pl.dryja.patternsearcher.matchingalgorithms.PatternProcessor;
import pl.dryja.patternsearcher.matchingalgorithms.ProcessorResult;
import pl.dryja.patternsearcher.persistance.PatternMatchingRepository;
import pl.dryja.patternsearcher.processingdelegates.UpdateDelegator;

import java.util.concurrent.Executor;

@Component
@AllArgsConstructor
@Slf4j
public class TaskManager implements UpdateDelegator {

    private final Executor executor;
    private final PatternMatchingRepository repository;

    public void runTask(final PatternProcessor processor) {

        log.info("Starting matching process for task id: {}", processor.getTaskId());
        processor.addUpdateDelegator(this);
        executor.execute(
                () -> processor.processInput()
        );
    }

    @Override
    public void processProgressed(final ProcessorResult result) {

        log.info("Update on task process: {}", result);
        final var taskOpt = repository.findById(result.getTaskId());
        final var task = taskOpt.orElseThrow(() -> new PatternMatchingTaskNotFoundException(result.getTaskId()));
        task.setPosition(result.getPosition());
        task.setTypos(result.getTypos());
        task.setProgress(result.getProgress());
        task.setFoundAnyPattern(result.isPartialOrFullPatternFound());
        repository.save(task);
    }
}