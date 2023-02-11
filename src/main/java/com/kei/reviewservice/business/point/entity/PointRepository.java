package com.kei.reviewservice.business.point.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByUserUserIdAndDeleteYn(String userId, Boolean deleteYn);
}
