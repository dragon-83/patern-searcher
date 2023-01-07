package pl.dryja.patternsearcher.rest;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProcessingResponse {
    private UUID taskId;
}
