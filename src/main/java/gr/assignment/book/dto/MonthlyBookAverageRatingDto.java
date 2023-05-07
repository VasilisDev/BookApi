package gr.assignment.book.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonthlyBookAverageRatingDto {
    private Long bookId;
    private List<yearlyMonthlyAverageRating> yearlyMonthlyAverageRatings;

    @Data
    public static class yearlyMonthlyAverageRating {
        private int year;
        private List<MonthlyAverageRating> monthlyAverageRatings;
    }

    @Data
    public static class MonthlyAverageRating {
        private int month;
        private double averageRating;

    }
}