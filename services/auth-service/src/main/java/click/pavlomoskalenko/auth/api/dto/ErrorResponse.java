package click.pavlomoskalenko.auth.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Map<String, String> fieldErrors;
}
