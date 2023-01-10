package pl.dryja.patternsearcher.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dryja.patternsearcher.errorhandling.ErrorResponse;
import pl.dryja.patternsearcher.persistance.PatternMatchingTask;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/patternsearcher")
public class TaskProcessingController {

    private final TaskProcessingService taskProcessingService;

    @Operation(
            summary = "Adding pattern matching task"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "403", description = "Out of threads in thread pool",
                    content = {
                    @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                            )}),
            @ApiResponse(responseCode = "400", description = "Wrong param type or body is not json type",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )})
    })
    @PostMapping
    public ResponseEntity<ProcessingResponse> runNewTask(@RequestBody @Valid final ProcessingRequest processingRequest) {

        log.info("Request for new task {}", processingRequest);
        final var taskId = taskProcessingService.startTask(processingRequest.getInput(), processingRequest.getPattern());
        final var response = ProcessingResponse.builder().taskId(taskId).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //TODO Need to add openapi annotation, for demo I added only one example
    @GetMapping("/status/{taskId}")
    public TaskStatusResponse checkTaskStatus(@PathVariable final UUID taskId) {

        log.info("Request for task status {}", taskId);
        final var progressPercentage = taskProcessingService.getTask(taskId);
        return TaskStatusResponse.builder().taskId(taskId).progress(progressPercentage.getProgress()).build();
    }

    //TODO Need to add openapi annotation, for demo I added only one example
    @GetMapping("/{taskId}")
    public PatternMatchingTask getTask(@PathVariable final UUID taskId) {

        log.info("Request for task {}", taskId);
        final var task = taskProcessingService.getTask(taskId);
        return task;
    }

    //TODO Need to add openapi annotation, for demo I added only one example
    @GetMapping("/all")
    public List<PatternMatchingTask> getAllTask() {

        log.info("Request for all tasks");
        final var tasks = taskProcessingService.getAllTask();
        return tasks;
    }
}
