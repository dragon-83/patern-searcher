package pl.dryja.patternsearcher.errorhandling;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.dryja.patternsearcher.exceptions.PatternMatchingTaskNotFoundException;

import java.util.concurrent.RejectedExecutionException;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Overall exception catch", e);
        return ResponseEntity.badRequest().body(
                new ErrorResponse("Something terribly went not ok, our IT team will be informed about it")
        );
    }

    @ExceptionHandler({ PatternMatchingTaskNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleNotFoundException(PatternMatchingTaskNotFoundException e) {
        log.error("Request for task, that do not exists");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({ RejectedExecutionException.class })
    public ResponseEntity<ErrorResponse> handleRejectExecutionException() {
        log.error("Execution reject to prevent problem increase thread pool config");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse("The execution pool is empty, please try one more time in few minutes if "
                        + "nothing change contact IT")
        );
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })//IllegalArgumentException
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException validException) {
        final var paramName = validException.getParameter().getParameterName();
        return ResponseEntity.badRequest().body(
                new ErrorResponse("Empty parameter " + paramName)
        );
    }

    @ExceptionHandler({ IllegalArgumentException.class, JsonParseException.class })
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException() {
        return ResponseEntity.badRequest().body(
                new ErrorResponse("Wrong param type or body is not json type")
        );
    }
}
