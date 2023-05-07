package gr.assignment.book.exception;

public class NoBooksFoundException extends RuntimeException {
    public NoBooksFoundException(String message) {
        super(message);
    }
}
