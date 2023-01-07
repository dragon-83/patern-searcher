package pl.dryja.patternsearcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.dryja.patternsearcher.configurations.ConfigProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class PatternsearcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatternsearcherApplication.class, args);
    }
}