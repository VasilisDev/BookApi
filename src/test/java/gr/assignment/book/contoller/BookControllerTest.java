package gr.assignment.book.contoller;

import gr.assignment.book.controller.BookController;
import gr.assignment.book.dto.BookDto;
import gr.assignment.book.dto.MonthlyBookAverageRatingDto;
import gr.assignment.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void searchBooks_ReturnsListOfBooks() {
        String title = "Sample title";
        int page = 1;
        int pageSize = 10;
        List<BookDto> expectedBooks = new ArrayList<>();
        expectedBooks.add(new BookDto());
        expectedBooks.add(new BookDto());
        when(bookService.searchBooksByTitle(title, page, pageSize)).thenReturn(expectedBooks);

        ResponseEntity<List<BookDto>> response = bookController.searchBooks(title, page, pageSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooks, response.getBody());
        verify(bookService, times(1)).searchBooksByTitle(title, page, pageSize);
    }

    @Test
    void getBookDetails_ReturnsBookDetails() {
        Long bookId = 1L;
        BookDto expectedBook = new BookDto();
        when(bookService.getBookDetails(bookId)).thenReturn(expectedBook);

        ResponseEntity<BookDto> response = bookController.getBookDetails(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBook, response.getBody());
        verify(bookService, times(1)).getBookDetails(bookId);
    }

    @Test
    void getTopRatedBooks_ReturnsListOfBooks() {
        int limit = 5;
        List<BookDto> expectedBooks = new ArrayList<>();
        expectedBooks.add(new BookDto());
        expectedBooks.add(new BookDto());
        when(bookService.getTopRatedBooks(limit)).thenReturn(expectedBooks);

        ResponseEntity<List<BookDto>> response = bookController.getTopRatedBooks(limit);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooks, response.getBody());
        verify(bookService, times(1)).getTopRatedBooks(limit);
    }

    @Test
    void getMonthlyAverageRatingByBookId_ReturnsMonthlyAverageRating() {
        Long bookId = 1L;
        MonthlyBookAverageRatingDto expectedRating = new MonthlyBookAverageRatingDto();
        when(bookService.getMonthlyRatingAverageBooksById(bookId)).thenReturn(expectedRating);

        ResponseEntity<MonthlyBookAverageRatingDto> response = bookController.getMonthlyAverageRatingByBookId(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRating, response.getBody());
        verify(bookService, times(1)).getMonthlyRatingAverageBooksById(bookId);
    }
}

