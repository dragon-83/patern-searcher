package pl.dryja.patternsearcher.taskmanagers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dryja.patternsearcher.matchingalgorithms.PatternProcessor;
import pl.dryja.patternsearcher.matchingalgorithms.ProcessorResult;

import java.util.UUID;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TaskManagerTest {

    @Mock
    private Executor executor;

    @Mock
    private PatternProcessor processor;

    @InjectMocks
    private TaskManager taskManager;

    @Test
    void whenAddedNewTaskThenExecutorExecute() {

        when(processor.getTaskId()).thenReturn(UUID.randomUUID());

        taskManager.runTask(processor);

        verify(executor, times(1)).execute(any(Runnable.class));
    }

    @Test
    void whenGetProcessingProgressGivenDataFromMapThenReturnIntValue() {

        final var taskId = UUID.randomUUID();
        when(processor.getTaskId()).thenReturn(taskId);
        taskManager.runTask(processor);

        final var result = taskManager.getPercentageProgress(taskId);

        assertThat(result).isNotNull();
    }

    @Test
    void whenGetProcessingProgressGivenNoDataFromMapThenReturnNull() {

        var result = taskManager.getPercentageProgress(UUID.randomUUID());

        assertThat(result).isNull();
    }

    @Test
    void whenProcessFinishedThenProcessResultRemovedFromMap() {
        final var taskId = UUID.randomUUID();
        when(processor.getTaskId()).thenReturn(taskId);
        when(processor.getProcessingResult())
                .thenReturn(new ProcessorResult(0, 5, true));

        taskManager.runTask(processor);
        taskManager.processFinish(taskId);
        var result = taskManager.getPercentageProgress(taskId);

        assertThat(result).isNull();
    }
}