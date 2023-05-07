package gr.assignment.book.error.handler;

import gr.assignment.book.error.ErrorResponse;
import gr.assignment.book.error.ValidationError;
import gr.assignment.book.exception.GutendexClientException;
import gr.assignment.book.exception.GutendexClientHttpErrorException;
import gr.assignment.book.exception.NoBooksFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<List<ValidationError>> handleValidationException(Exception ex) {
        log.error("handling validation error", ex);
        List<ValidationError> errors = new ArrayList<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().forEach(error -> {
                String field = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                errors.add(new ValidationError(field, message));
            });
        } else if (ex instanceof ConstraintViolationException) {
            ((ConstraintViolationException) ex).getConstraintViolations().forEach(violation -> {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                errors.add(new ValidationError(field, message));
            });
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NoBooksFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoBooksFoundException(NoBooksFoundException ex) {
        log.error("Handling books not found exception with message: {}", ex.getMessage());
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(httpStatus).body(new ErrorResponse(ex.getMessage(), httpStatus.value()));
    }

    @ExceptionHandler(GutendexClientHttpErrorException.class)
    protected ResponseEntity<ErrorResponse> handleGutendexClientHttpClientErrorException(GutendexClientHttpErrorException ex) {
        log.error("Handling gutendex client error exception with message: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(new ErrorResponse(ex.getDetail(), ex.getStatusCode()));
    }

    @ExceptionHandler(GutendexClientException.class)
    public ResponseEntity<ErrorResponse> handleGutendexClientException(GutendexClientException ex) {
        log.error("handling unknown gutendex error", ex);
        return ResponseEntity.internalServerError().body(new ErrorResponse("Books provider returns the following error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("handling unexpected error", ex);
        return ResponseEntity.internalServerError().body(new ErrorResponse("An error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}