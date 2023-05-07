package gr.assignment.book.contoller;

import gr.assignment.book.controller.ReviewController;
import gr.assignment.book.dto.ReviewDto;
import gr.assignment.book.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @Test
    void reviewBook_ValidReviewDto_CallsSaveReviewAndReturnsOkResponse() {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setRating(4);
        reviewDto.setReviewText("Great book");

        ResponseEntity<?> response = reviewController.reviewBook(reviewDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(reviewService, times(1)).saveReview(reviewDto);
    }
}

