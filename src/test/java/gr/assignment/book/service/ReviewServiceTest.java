package gr.assignment.book.service;

import gr.assignment.book.dto.ReviewDto;
import gr.assignment.book.mapper.ReviewMapper;
import gr.assignment.book.model.Review;
import gr.assignment.book.projection.MonthlyBookAverageRatingProjection;
import gr.assignment.book.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void getAll_ReturnsListOfReviewDtos() {
        Review review1 = new Review();
        review1.setId(1L);
        review1.setRating(4);
        Review review2 = new Review();
        review2.setId(2L);
        review2.setRating(5);
        List<Review> reviews = Arrays.asList(review1, review2);

        ReviewDto reviewDto1 = new ReviewDto();
        reviewDto1.setBookId(1L);
        reviewDto1.setRating(4);

        ReviewDto reviewDto2 = new ReviewDto();
        reviewDto2.setBookId(2L);
        reviewDto2.setRating(5);

        List<ReviewDto> expectedReviewDtos = Arrays.asList(reviewDto1, reviewDto2);

        when(reviewRepository.findAll()).thenReturn(reviews);
        when(reviewMapper.toDto(review1)).thenReturn(reviewDto1);
        when(reviewMapper.toDto(review2)).thenReturn(reviewDto2);

        List<ReviewDto> result = reviewService.getAll();

        assertEquals(expectedReviewDtos, result);

        verify(reviewRepository, times(1)).findAll();
        verify(reviewMapper, times(1)).toDto(review1);
        verify(reviewMapper, times(1)).toDto(review2);
    }

    @Test
    void saveReview_CallsReviewMapperAndReviewRepository() {
        ReviewDto reviewDto = new ReviewDto();
        Review review = new Review();
        when(reviewMapper.toEntity(reviewDto)).thenReturn(review);

        reviewService.saveReview(reviewDto);

        verify(reviewMapper, times(1)).toEntity(reviewDto);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void findReviewsByBookId_ReturnsListOfReviewDtos() {
        Long bookId = 1L;
        Review review1 = new Review();
        review1.setId(1L);
        review1.setRating(4);
        Review review2 = new Review();
        review2.setId(2L);
        review2.setRating(5);
        List<Review> reviews = Arrays.asList(review1, review2);

        ReviewDto reviewDto1 = new ReviewDto();
        reviewDto1.setBookId(1L);
        reviewDto1.setRating(4);
        ReviewDto reviewDto2 = new ReviewDto();
        reviewDto2.setBookId(2L);
        reviewDto2.setRating(5);
        List<ReviewDto> expectedReviewDtos = Arrays.asList(reviewDto1, reviewDto2);

        when(reviewRepository.findByBookId(bookId)).thenReturn(reviews);
        when(reviewMapper.toDto(review1)).thenReturn(reviewDto1);
        when(reviewMapper.toDto(review2)).thenReturn(reviewDto2);

        List<ReviewDto> result = reviewService.findReviewsByBookId(bookId);

        assertEquals(expectedReviewDtos, result);

        verify(reviewRepository, times(1)).findByBookId(bookId);
        verify(reviewMapper, times(1)).toDto(review1);
        verify(reviewMapper, times(1)).toDto(review2);
    }

    @Test
    void getMonthlyRatingAverageByBookId_ReturnsListOfMonthlyBookAverageRatingProjections() {
        Long bookId = 1L;
        MonthlyBookAverageRatingProjection projection1 = mock(MonthlyBookAverageRatingProjection.class);
        MonthlyBookAverageRatingProjection projection2 = mock(MonthlyBookAverageRatingProjection.class);
        List<MonthlyBookAverageRatingProjection> expectedProjections = Arrays.asList(projection1, projection2);

        when(reviewRepository.findMonthlyAverageRatingByBookId(bookId)).thenReturn(expectedProjections);

        List<MonthlyBookAverageRatingProjection> result = reviewService.getMonthlyRatingAverageByBookId(bookId);

        assertEquals(expectedProjections, result);

        verify(reviewRepository, times(1)).findMonthlyAverageRatingByBookId(bookId);
    }
}
