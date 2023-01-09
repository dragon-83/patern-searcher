package pl.dryja.patternsearcher.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.dryja.patternsearcher.persistance.PatternMatchingTask;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/patternsearcher")
public class TaskProcessingController {

    private final TaskProcessingService taskProcessingService;

    @PostMapping
    public ProcessingResponse runNewTask(@RequestBody @Valid final ProcessingRequest processingRequest) {

        log.info("Request for new task {}", processingRequest);
        final var taskId = taskProcessingService.startTask(processingRequest.getInput(), processingRequest.getPattern());
        return ProcessingResponse.builder().taskId(taskId).build();
    }

    @GetMapping("/status/{taskId}")
    public TaskStatusResponse checkTaskStatus(@PathVariable final UUID taskId) {

        log.info("Request for task status {}", taskId);
        final var progressPercentage = taskProcessingService.getTask(taskId);
        return TaskStatusResponse.builder().taskId(taskId).progress(progressPercentage.getProgress()).build();
    }

    @GetMapping("/{taskId}")
    public PatternMatchingTask getTask(@PathVariable final UUID taskId) {

        log.info("Request for task {}", taskId);
        final var task = taskProcessingService.getTask(taskId);
        return task;
    }

    @GetMapping("/all")
    public List<PatternMatchingTask> getAllTask() {

        log.info("Request for all tasks");
        final var tasks = taskProcessingService.getAllTask();
        return tasks;
    }
}
