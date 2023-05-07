package gr.assignment.book.service;

import gr.assignment.book.client.GutendexClient;
import gr.assignment.book.dto.BookDto;
import gr.assignment.book.dto.MonthlyBookAverageRatingDto;
import gr.assignment.book.dto.ReviewDto;
import gr.assignment.book.projection.MonthlyBookAverageRatingProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private GutendexClient gutendexClient;

    @Mock
    private ReviewService reviewService;

    @Test
    void searchBooksByTitle_ReturnsListOfBooks() {
        String title = "Java";
        int page = 1;
        int pageSize = 10;
        List<BookDto> expectedBooks = Arrays.asList(new BookDto(), new BookDto());

        when(gutendexClient.searchBooks(eq(title), eq(page), eq(pageSize))).thenReturn(expectedBooks);

        List<BookDto> actualBooks = bookService.searchBooksByTitle(title, page, pageSize);

        assertEquals(expectedBooks, actualBooks);
    }

    @Test
    void getBookDetails_ReturnsBookWithReviewsAndAverageRating() {
        Long bookId = 1L;
        BookDto expectedBook = new BookDto();
        expectedBook.setId(bookId);
        List<ReviewDto> reviews = Arrays.asList(
                createReviewDto(1L, 4),
                createReviewDto(2L, 5)
        );

        when(gutendexClient.getBookById(eq(bookId))).thenReturn(expectedBook);
        when(reviewService.findReviewsByBookId(eq(bookId))).thenReturn(reviews);

        BookDto actualBook = bookService.getBookDetails(bookId);

        assertEquals(expectedBook, actualBook);
        assertEquals(reviews.size(), actualBook.getReviews().size());
        assertEquals(4.5, actualBook.getRating());
    }

    @Test
    void getTopRatedBooks_ReturnsListOfTopRatedBooks() {
        int limit = 2;
        List<ReviewDto> reviews = Arrays.asList(
                createReviewDto(1L, 5),
                createReviewDto(1L, 4),
                createReviewDto(2L, 3),
                createReviewDto(2L, 5),
                createReviewDto(3L, 3),
                createReviewDto(3L, 5),
                createReviewDto(4L, 5),
                createReviewDto(4L, 5)
        );

        when(reviewService.getAll()).thenReturn(reviews);

        BookDto book1 = createBookDto(1L);
        BookDto book3 = createBookDto(3L);
        BookDto book4 = createBookDto(4L);

        when(gutendexClient.getAll()).thenReturn(List.of(book1, book3, book4));

        List<BookDto> topRatedBooks = bookService.getTopRatedBooks(limit);

        assertEquals(limit, topRatedBooks.size());
        assertEquals(book4, topRatedBooks.get(0));
        assertEquals(book1, topRatedBooks.get(1));
    }

    @Test
    void getMonthlyRatingAverageBooksById_ReturnsMonthlyBookAverageRatingDto() {
        Long bookId = 1L;
        List<MonthlyBookAverageRatingProjection> projections = Arrays.asList(
                createMonthlyBookAverageRatingProjection(bookId, 2023, 1, 4.5),
                createMonthlyBookAverageRatingProjection(bookId, 2023, 2, 4.2),
                createMonthlyBookAverageRatingProjection(bookId, 2024, 1, 4.0)
        );

        when(reviewService.getMonthlyRatingAverageByBookId(eq(bookId))).thenReturn(projections);

        MonthlyBookAverageRatingDto actualDto = bookService.getMonthlyRatingAverageBooksById(bookId);

        assertEquals(bookId, actualDto.getBookId());
        assertEquals(2, actualDto.getYearlyMonthlyAverageRatings().size());
        assertEquals(2, actualDto.getYearlyMonthlyAverageRatings().get(0).getMonthlyAverageRatings().size());
        assertEquals(1, actualDto.getYearlyMonthlyAverageRatings().get(1).getMonthlyAverageRatings().size());


        assertEquals(2023, actualDto.getYearlyMonthlyAverageRatings().get(0).getYear());
        assertEquals(1, actualDto.getYearlyMonthlyAverageRatings().get(0).getMonthlyAverageRatings().get(0).getMonth());
        assertEquals(4.5, actualDto.getYearlyMonthlyAverageRatings().get(0).getMonthlyAverageRatings().get(0).getAverageRating());
        assertEquals(2, actualDto.getYearlyMonthlyAverageRatings().get(0).getMonthlyAverageRatings().get(1).getMonth());
        assertEquals(4.2, actualDto.getYearlyMonthlyAverageRatings().get(0).getMonthlyAverageRatings().get(1).getAverageRating());

        assertEquals(2024, actualDto.getYearlyMonthlyAverageRatings().get(1).getYear());
        assertEquals(1, actualDto.getYearlyMonthlyAverageRatings().get(1).getMonthlyAverageRatings().get(0).getMonth());
        assertEquals(4.0, actualDto.getYearlyMonthlyAverageRatings().get(1).getMonthlyAverageRatings().get(0).getAverageRating());
    }


    private ReviewDto createReviewDto(Long bookId, Integer rating) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setBookId(bookId);
        reviewDto.setRating(rating);
        reviewDto.setReviewText("Great book!");
        return reviewDto;
    }

    private BookDto createBookDto(Long bookId) {
        return createBookDto(bookId, null);
    }

    private BookDto createBookDto(Long bookId, Double rate) {
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        if (rate != null) {
            bookDto.setRating(rate);

        }
        bookDto.setTitle("Sample Book");
        return bookDto;
    }

    private MonthlyBookAverageRatingProjection createMonthlyBookAverageRatingProjection(Long bookId, int year, int month, double averageRating) {
        MonthlyBookAverageRatingProjection projection = mock(MonthlyBookAverageRatingProjection.class);
        lenient().when(projection.getBookId()).thenReturn(bookId);
        lenient().when(projection.getYear()).thenReturn(year);
        lenient().when(projection.getMonth()).thenReturn(month);
        lenient().when(projection.getAverageRating()).thenReturn(averageRating);
        return projection;
    }

}
