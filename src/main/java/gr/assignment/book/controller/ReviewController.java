package gr.assignment.book.controller;

import gr.assignment.book.dto.ReviewDto;
import gr.assignment.book.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> reviewBook(@Valid @RequestBody ReviewDto reviewDto) {
        reviewService.saveReview(reviewDto);
        return ResponseEntity.ok().build();
    }
}
