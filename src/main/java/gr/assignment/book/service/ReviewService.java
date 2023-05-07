package gr.assignment.book.service;

import gr.assignment.book.dto.ReviewDto;
import gr.assignment.book.mapper.ReviewMapper;
import gr.assignment.book.model.Review;
import gr.assignment.book.projection.MonthlyBookAverageRatingProjection;
import gr.assignment.book.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;

    public List<ReviewDto> getAll() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveReview(ReviewDto reviewDto) {
        Review review = reviewMapper.toEntity(reviewDto);
        reviewRepository.save(review);
    }

    public List<ReviewDto> findReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId)
                .stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<MonthlyBookAverageRatingProjection> getMonthlyRatingAverageByBookId(Long bookId) {
        return reviewRepository.findMonthlyAverageRatingByBookId(bookId);
    }
}