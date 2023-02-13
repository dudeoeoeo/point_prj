package com.kei.reviewservice.business.review.entity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByPlaceIdAndUserUserIdAndDeleteYn(Long placeId, String userId, Boolean deleteYn);
    @EntityGraph(attributePaths = {"user", "place"})
    Optional<Review> findByReviewIdAndDeleteYn(String reviewId, Boolean deleteYn);

    List<Review> findAllByPlace_PlaceIdAndDeleteYn(String placeId, Boolean deleteYn);
    List<Review> findAllByPlaceIdAndDeleteYn(String placeId, Boolean deleteYn);
}
