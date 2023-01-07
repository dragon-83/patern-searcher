package pl.dryja.patternsearcher.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.dryja.patternsearcher.matchingalgorithms.PatternProcessor;
import pl.dryja.patternsearcher.taskmanagers.TaskManager;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class TaskProcessingService {

    private final TaskManager taskManager;

    public UUID startTask(final String input, final String patter) {

        final var taskId = UUID.randomUUID();
        log.info("Create new task, id: {}", taskId);
        final var processor = new PatternProcessor(taskId, patter, input);
        taskManager.runTask(processor);
        return taskId;
    }

    public Integer taskProgressPercentage(final UUID taskId) {

        final var progressPercentage = taskManager.getPercentageProgress(taskId);
        return Objects.isNull(progressPercentage) ? null : progressPercentage.progressInPercentage();
    }
}
