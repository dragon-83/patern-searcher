package pl.dryja.patternsearcher.integration;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pl.dryja.patternsearcher.configurations.ExecutorConfig;
import pl.dryja.patternsearcher.persistance.PatternMatchingRepository;
import pl.dryja.patternsearcher.persistance.PatternMatchingTask;
import pl.dryja.patternsearcher.rest.ProcessingRequest;
import pl.dryja.patternsearcher.rest.ProcessingResponse;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.dryja.patternsearcher.TestUtil.asJsonString;
import static pl.dryja.patternsearcher.TestUtil.jsonBytesToObject;

//TODO create a profile IntegrationTest and proper annotation for it
@WebMvcTest
@ComponentScan(basePackages = "pl.dryja.patternsearcher")
@ContextConfiguration(classes = {ExecutorConfig.class})
class TaskProcessingIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PatternMatchingRepository patternMatchingRepository;

    @Test
    void runTask() throws Exception {

        final var pattern = "AA";
        final var input = "BBAA";
        final var bArray = mvc.perform(
                post("/patternsearcher")
                        .content(asJsonString(new ProcessingRequest(pattern, input)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").isNotEmpty())
                .andReturn().getResponse().getContentAsByteArray();
        final var response = jsonBytesToObject(bArray, ProcessingResponse.class);

        when(patternMatchingRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(new PatternMatchingTask(response.getTaskId(), input, pattern)));
        when(patternMatchingRepository.findById(any(UUID.class)))
                .thenAnswer(invocation -> Optional.of(new PatternMatchingTask(response.getTaskId(), input, pattern)));

        await().atMost(Duration.ofSeconds(6)).with().pollDelay(5, TimeUnit.SECONDS).untilAsserted(
                () -> {
                    final var taskArgumentCaptor = ArgumentCaptor.forClass(PatternMatchingTask.class);
                    verify(patternMatchingRepository, times(5)).save(taskArgumentCaptor.capture());
                    assertThat(
                            taskArgumentCaptor.getAllValues().stream().filter(task -> task.getProgress().compareTo(100) < 0).count()
                    ).isEqualTo(4);
                }
        );
    }

    //TODO should test all exceptions and other invocation, but for this demo should be enough :)
}