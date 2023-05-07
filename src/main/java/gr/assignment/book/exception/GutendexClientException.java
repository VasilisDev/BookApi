package gr.assignment.book.exception;

public class GutendexClientException extends RuntimeException {

    public GutendexClientException(String message) {
        super(message);
    }

    public GutendexClientException(Throwable throwable) {
        super(throwable);
    }

    public GutendexClientException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
