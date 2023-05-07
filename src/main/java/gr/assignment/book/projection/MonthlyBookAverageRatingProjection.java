package gr.assignment.book.projection;

public interface MonthlyBookAverageRatingProjection {
    Integer getYear();
    Integer getMonth();
    Double getAverageRating();
    Long getBookId();
}