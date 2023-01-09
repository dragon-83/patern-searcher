package pl.dryja.patternsearcher.taskmanagers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dryja.patternsearcher.exceptions.PatternMatchingTaskNotFoundException;
import pl.dryja.patternsearcher.matchingalgorithms.PatternProcessor;
import pl.dryja.patternsearcher.matchingalgorithms.ProcessorResult;
import pl.dryja.patternsearcher.persistance.PatternMatchingRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskManagerTest {

    @Mock
    private Executor executor;

    @Mock
    private PatternProcessor processor;

    @Mock
    private PatternMatchingRepository repository;

    @InjectMocks
    private TaskManager taskManager;

    @Test
    void whenAddedNewTaskThenExecutorExecute() {

        when(processor.getTaskId()).thenReturn(UUID.randomUUID());

        taskManager.runTask(processor);

        verify(executor, times(1)).execute(any(Runnable.class));
    }

    @Test
    void whenSaveResultWhereTaskNotFoundInDbThenThrowException() {

        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskManager.processProgressed(new ProcessorResult(UUID.randomUUID())))
                .isInstanceOf(PatternMatchingTaskNotFoundException.class);
    }
}