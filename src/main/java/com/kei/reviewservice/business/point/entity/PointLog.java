package com.kei.reviewservice.business.point.entity;

import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.common.constant.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "point_log",
        indexes = @Index(
            name = "idx_review_id",
            columnList = "review_id"
        )
)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id")
    private String reviewId;

    @Column(name = "photoYn")
    private Boolean photoYn;

    @Column(name = "point")
    private int point;

    public static PointLog createPointLog(EventReviewReq req, int point) {
        return PointLog.builder()
                .reviewId(req.getReviewId())
                .photoYn(!req.getAttachedPhotoIds().isEmpty())
                .point(point)
                .build();
    }

}
