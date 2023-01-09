package pl.dryja.patternsearcher.persistance;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("patternmatchingtasks")
public class PatternMatchingTask {

    public PatternMatchingTask(UUID id, String input, String pattern) {
        this.id = id;
        this.input = input;
        this.pattern = pattern;
        this.foundAnyPattern = false;
        this.progress = 0;
    }

    @Id
    private UUID id;

    private String input;
    private String pattern;
    private Boolean foundAnyPattern;
    private Integer typos;
    private Integer position;
    private Integer progress;
}
