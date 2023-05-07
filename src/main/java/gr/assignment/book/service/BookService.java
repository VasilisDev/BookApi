package gr.assignment.book.service;

import gr.assignment.book.client.GutendexClient;
import gr.assignment.book.dto.BookDto;
import gr.assignment.book.dto.MonthlyBookAverageRatingDto;
import gr.assignment.book.dto.ReviewDto;
import gr.assignment.book.exception.NoBooksFoundException;
import gr.assignment.book.projection.MonthlyBookAverageRatingProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final GutendexClient gutendexClient;
    private final ReviewService reviewService;

    @Cacheable(cacheNames = "${cache.book.search.name}", key = "{#title, (#page ?: 0), (#pageSize ?: 0)}", unless = "#result == null")
    public List<BookDto> searchBooksByTitle(String title, Integer page, Integer pageSize) {
        return gutendexClient.searchBooks(title, page, pageSize);
    }

    @Cacheable(value = "${cache.book.name}", key = "#bookId")
    public BookDto getBookDetails(Long bookId) {
        BookDto bookDto = gutendexClient.getBookById(bookId);

        if (bookDto == null) {
            throw new NoBooksFoundException("Book with id: " + bookId + " not found.");
        }

        List<ReviewDto> reviews = this.reviewService.findReviewsByBookId(bookId);

        double avgRating = reviews.stream()
                .mapToDouble(ReviewDto::getRating)
                .average()
                .orElse(0);

        List<String> bookReviews = reviews.stream()
                .map(ReviewDto::getReviewText)
                .collect(Collectors.toList());

        bookDto.setReviews(bookReviews);
        bookDto.setRating(avgRating);

        return bookDto;
    }

    public List<BookDto> getTopRatedBooks(int limit) {
        List<ReviewDto> reviews = reviewService.getAll();
        Map<Long, List<ReviewDto>> reviewsByBookId = reviews.stream()
                .collect(Collectors.groupingBy(ReviewDto::getBookId));

        Map<Long, Double> avgRatingsByBookId = new HashMap<>();
        for (Long bookId : reviewsByBookId.keySet()) {
            List<ReviewDto> bookReviews = reviewsByBookId.get(bookId);
            double avgRating = bookReviews.stream()
                    .mapToDouble(ReviewDto::getRating)
                    .average()
                    .orElse(0.0);
            avgRatingsByBookId.put(bookId, avgRating);
        }

        List<Map.Entry<Long, Double>> sortedEntries = avgRatingsByBookId.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());

        List<BookDto> topRatedBooks = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : sortedEntries) {
            BookDto book = this.getBookDetails(entry.getKey());
            topRatedBooks.add(book);
        }

        return topRatedBooks;
    }

    public MonthlyBookAverageRatingDto getMonthlyRatingAverageBooksById(Long bookId) {
        return projectionToDto(reviewService.getMonthlyRatingAverageByBookId(bookId));
    }

    private MonthlyBookAverageRatingDto projectionToDto(List<MonthlyBookAverageRatingProjection> projections) {
        MonthlyBookAverageRatingDto dto = new MonthlyBookAverageRatingDto();

        if (projections.isEmpty()) {
            return dto;
        }

        dto.setBookId(projections.get(0).getBookId());
        dto.setYearlyMonthlyAverageRatings(new ArrayList<>());

        MonthlyBookAverageRatingDto.yearlyMonthlyAverageRating yearlyMonthlyAverageRatings = null;
        int currentYear = -1;

        for (MonthlyBookAverageRatingProjection projection : projections) {
            int year = projection.getYear();
            int month = projection.getMonth();
            double averageRating = projection.getAverageRating();
            if (year != currentYear) {
                yearlyMonthlyAverageRatings = new MonthlyBookAverageRatingDto.yearlyMonthlyAverageRating();
                yearlyMonthlyAverageRatings.setYear(year);
                yearlyMonthlyAverageRatings.setMonthlyAverageRatings(new ArrayList<>());
                dto.getYearlyMonthlyAverageRatings().add(yearlyMonthlyAverageRatings);

                currentYear = year;
            }

            if (yearlyMonthlyAverageRatings != null) {
                MonthlyBookAverageRatingDto.MonthlyAverageRating monthlyAverageRating = new MonthlyBookAverageRatingDto.MonthlyAverageRating();
                monthlyAverageRating.setMonth(month);
                monthlyAverageRating.setAverageRating(averageRating);

                yearlyMonthlyAverageRatings.getMonthlyAverageRatings().add(monthlyAverageRating);
            }
        }

        return dto;
    }
}
