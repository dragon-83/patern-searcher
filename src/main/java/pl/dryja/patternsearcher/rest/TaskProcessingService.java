package pl.dryja.patternsearcher.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.dryja.patternsearcher.exceptions.PatternMatchingTaskNotFoundException;
import pl.dryja.patternsearcher.matchingalgorithms.PatternProcessor;
import pl.dryja.patternsearcher.persistance.PatternMatchingRepository;
import pl.dryja.patternsearcher.persistance.PatternMatchingTask;
import pl.dryja.patternsearcher.taskmanagers.TaskManager;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class TaskProcessingService {

    private final TaskManager taskManager;
    private final PatternMatchingRepository repository;

    public UUID startTask(final String input, final String patter) {

        final var taskId = UUID.randomUUID();
        log.info("Create new task, id: {}", taskId);
        final var processor = new PatternProcessor(taskId, patter, input);
        repository.save(new PatternMatchingTask(taskId, input, patter));
        taskManager.runTask(processor);
        return taskId;
    }

    public PatternMatchingTask getTask(final UUID taskId) {

        log.info("Search task in task repo, id: {}", taskId);
        final var taskFromRepoOpt = repository.findById(taskId);
        return taskFromRepoOpt.orElseThrow(() -> new PatternMatchingTaskNotFoundException(taskId));
    }

    public List<PatternMatchingTask> getAllTask() {

        //TODO should add pagination, but for demo I only mention about it
        log.info("Get all task from repo");
        final var taskInRepo = repository.findAll();
        return taskInRepo;
    }
}
