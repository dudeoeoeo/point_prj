package com.kei.reviewservice.business.review.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByPlaceIdAndUserIdAndDeleteYn(Long placeId, String userId, Boolean deleteYn);
    Optional<Review> findByReviewIdAndDeleteYn(String reviewId, Boolean deleteYn);

    List<Review> findAllByPlaceIdAndDeleteYn(String placeId, Boolean deleteYn);
}
