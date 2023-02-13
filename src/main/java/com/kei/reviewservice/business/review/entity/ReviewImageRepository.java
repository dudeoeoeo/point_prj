package com.kei.reviewservice.business.review.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    ReviewImage findByReviewImageId(String reviewImageId);
}
