package gr.assignment.book.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReviewDto {
    @NotNull(message = "${review.message.bookId}")
    private Long bookId;

    @Min(value = 0, message = "${review.message.rating.min}")
    @Max(value = 5, message = "${review.message.rating.max}")
    private Integer rating;

    @NotBlank(message = "${review.message.reviewText}")
    private String reviewText;
}
