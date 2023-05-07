package gr.assignment.book.controller;

import gr.assignment.book.dto.BookDto;
import gr.assignment.book.dto.MonthlyBookAverageRatingDto;
import gr.assignment.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> searchBooks(@RequestParam String title,
                                                     @Positive(message = "{positive.page.number.message}")
                                                     @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer pageSize) {
        List<BookDto> books = bookService.searchBooksByTitle(title, page, pageSize);
        return ResponseEntity.ok().body(books);
    }


    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBookDetails(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookDetails(bookId));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<BookDto>> getTopRatedBooks(@Positive(message = "{positive.limit.message}") @RequestParam Integer limit) {
        return ResponseEntity.ok(bookService.getTopRatedBooks(limit));
    }

    @GetMapping("/{bookId}/monthly-average-rating")
    public ResponseEntity<MonthlyBookAverageRatingDto> getMonthlyAverageRatingByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getMonthlyRatingAverageBooksById(bookId));
    }
}