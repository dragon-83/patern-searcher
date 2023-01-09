package pl.dryja.patternsearcher.rest;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingRequest {

    @NotBlank
    private String pattern;

    @NotBlank
    private String input;
}
