package pl.dryja.patternsearcher.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "patternsearcher")
public class ConfigProperties {

    public final Executor executor = new Executor();

    @Getter
    @Setter
    public static class Executor {
        private int maxPoolSize;
        private int corePoolSize;
        private int queueCapacity;
    }
}