package pl.dryja.patternsearcher.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@AllArgsConstructor
public class ExecutorConfig {

    private final ConfigProperties configProperties;

    @Bean
    public Executor taskExecutor() {

        final var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(configProperties.getExecutor().getCorePoolSize());
        executor.setMaxPoolSize(configProperties.getExecutor().getMaxPoolSize());
        executor.setQueueCapacity(configProperties.getExecutor().getQueueCapacity());
        executor.setThreadNamePrefix("pattern-match-thread-");
        executor.initialize();
        return executor;
    }
}