package gr.assignment.book.repository;

import gr.assignment.book.model.Review;
import gr.assignment.book.projection.MonthlyBookAverageRatingProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookId(Long bookId);

    @Query("SELECT r.bookId AS bookId, " +
            "       YEAR(r.createdAt) AS year, " +
            "       MONTH(r.createdAt) AS month, " +
            "       AVG(r.rating) AS averageRating " +
            "FROM Review r " +
            "WHERE r.bookId = :bookId " +
            "GROUP BY r.bookId, YEAR(r.createdAt), MONTH(r.createdAt) " +
            "ORDER BY YEAR(r.createdAt) ASC, MONTH(r.createdAt) ASC")
    List<MonthlyBookAverageRatingProjection> findMonthlyAverageRatingByBookId(@Param("bookId") Long bookId);
}