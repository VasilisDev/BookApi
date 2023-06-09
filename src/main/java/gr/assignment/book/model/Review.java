package gr.assignment.book.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "book_id")
    private Long bookId;

    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "rating")
    private Integer rating;

    @NotBlank
    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();
}
