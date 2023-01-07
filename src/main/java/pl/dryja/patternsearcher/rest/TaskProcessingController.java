package pl.dryja.patternsearcher.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/patternsearcher")
public class TaskProcessingController {

    private final TaskProcessingService taskProcessingService;

    @PostMapping
    public ProcessingResponse runNewTask(@RequestBody @Valid ProcessingRequest processingRequest) {

        log.info("Request for new task {}", processingRequest);
        final var taskId = taskProcessingService.startTask(processingRequest.getInput(), processingRequest.getPattern());
        return ProcessingResponse.builder().taskId(taskId).build();
    }

    @GetMapping("/status/{taskId}")
    public TaskStatusResponse checkTaskStatus(@PathVariable UUID taskId) {

        log.info("Request for task status {}", taskId);
        final var progressPercentage = taskProcessingService.taskProgressPercentage(taskId);
        return TaskStatusResponse.builder().taskId(taskId).progress(progressPercentage).build();
    }
}
