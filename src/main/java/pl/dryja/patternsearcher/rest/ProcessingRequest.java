package pl.dryja.patternsearcher.rest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProcessingRequest {

    @NotBlank
    private String pattern;

    @NotBlank
    private String input;
}
