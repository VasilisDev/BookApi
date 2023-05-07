package gr.assignment.book.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

@Slf4j
@Getter
public class GutendexClientHttpErrorException extends RuntimeException {
    private final String responseBody;
    private final ObjectMapper objectMapper;
    private final int statusCode;


    public GutendexClientHttpErrorException(HttpStatusCodeException clientErrorException) {
        super(clientErrorException);
        this.responseBody = clientErrorException.getResponseBodyAsString();
        this.statusCode = clientErrorException.getRawStatusCode();
        this.objectMapper = new ObjectMapper();
    }

    public String getDetail() {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            return root.get("detail").asText();
        } catch (IOException e) {
            log.warn("Could not parse detail attribute \"detail\" of gutendex...", e);
            return null;
        }
    }
}

