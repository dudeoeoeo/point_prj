package com.kei.reviewservice.business.point.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    List<PointLog> findAllByReviewIdAndDeleteYnOrderByIdDesc(String reviewId, Boolean deleteYn);
    List<PointLog> findAllByReviewIdOrderByIdDesc(String reviewId);
    List<PointLog> findAllByUserIdAndDeleteYn(String userId, Boolean deleteYn);
    Optional<PointLog> findByReviewIdAndDeleteYn(String reviewId, Boolean deleteYn);
}
