package gr.assignment.book.mapper;

import gr.assignment.book.dto.ReviewDto;
import gr.assignment.book.model.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewDto toDto(Review review);

    Review toEntity(ReviewDto reviewDto);

}
