package gr.assignment.book.error.handler;


import gr.assignment.book.error.ErrorResponse;
import gr.assignment.book.error.ValidationError;
import gr.assignment.book.exception.GutendexClientException;
import gr.assignment.book.exception.GutendexClientHttpErrorException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionHandlerAdviceTest {

    @Test
    void handleValidationException_WithMethodArgumentNotValidException_ReturnsBadRequestWithValidationErrors() throws Exception {
        FieldError fieldError = mock(FieldError.class);
        when(fieldError.getField()).thenReturn("fieldName");
        when(fieldError.getDefaultMessage()).thenReturn("Field is required");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = createMethodArgumentNotValidException(bindingResult);

        ExceptionHandlerAdvice advice = new ExceptionHandlerAdvice();

        ResponseEntity<List<ValidationError>> response = advice.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        List<ValidationError> errors = response.getBody();
        assertEquals(1, errors.size());
        ValidationError error = errors.get(0);
        assertEquals("fieldName", error.getField());
        assertEquals("Field is required", error.getMessage());
    }

    @Test
    void handleValidationException_WithConstraintViolationException_ReturnsBadRequestWithValidationErrors() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);

        Path path = mock(Path.class);
        when(path.toString()).thenReturn("fieldName");

        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("Field must not be empty");

        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        Set<ConstraintViolation<?>> violations = Set.of(violation);
        when(ex.getConstraintViolations()).thenReturn(violations);

        ExceptionHandlerAdvice advice = new ExceptionHandlerAdvice();

        ResponseEntity<List<ValidationError>> response = advice.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        List<ValidationError> errors = response.getBody();
        assertEquals(1, errors.size());
        ValidationError error = errors.get(0);
        assertEquals("fieldName", error.getField());
        assertEquals("Field must not be empty", error.getMessage());
    }

    @Test
    void handleGutendexClientHttpClientErrorException_ReturnsNotFoundWithErrorResponse() {
        String invalidPageResponse =  "{\"detail\":\"Invalid page.\"}";
        HttpClientErrorException e = new HttpClientErrorException(HttpStatus.NOT_FOUND, "404", invalidPageResponse.getBytes(), Charset.defaultCharset());
        GutendexClientHttpErrorException ex = new GutendexClientHttpErrorException(e);

        ExceptionHandlerAdvice advice = new ExceptionHandlerAdvice();

        ResponseEntity<ErrorResponse> response = advice.handleGutendexClientHttpClientErrorException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertEquals("Invalid page.", errorResponse.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatusCode());
    }

    @Test
    void handleGutendexClientException_ReturnsInternalServerErrorWithErrorResponse() {
        // Arrange
        GutendexClientException ex = new GutendexClientException("Internal Server Error");

        ExceptionHandlerAdvice advice = new ExceptionHandlerAdvice();

        // Act
        ResponseEntity<ErrorResponse> response = advice.handleGutendexClientException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertEquals("Books provider returns the following error: Internal Server Error", errorResponse.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatusCode());

    }

    @Test
    void handleException_ReturnsInternalServerErrorWithErrorResponse() {
        Exception ex = new Exception("Unexpected error");
        ExceptionHandlerAdvice advice = new ExceptionHandlerAdvice();

        ResponseEntity<ErrorResponse> response = advice.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertEquals("An error occurred. Please try again later.", errorResponse.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatusCode());

    }

    private MethodArgumentNotValidException createMethodArgumentNotValidException(BindingResult bindingResult) throws Exception {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        Field bindingResultField = BindException.class.getDeclaredField("bindingResult");
        bindingResultField.setAccessible(true);
        bindingResultField.set(ex, bindingResult);
        return ex;
    }
}
