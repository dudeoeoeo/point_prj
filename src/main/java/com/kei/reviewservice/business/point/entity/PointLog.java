package com.kei.reviewservice.business.point.entity;

import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.common.constant.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
        name = "point_log",
        indexes = {
                @Index(
                        name = "idx_review_id",
                        columnList = "review_id"
                ),
                @Index(
                        name = "idx_user_id",
                        columnList = "user_id"
                )
        }
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

    @Column(name = "user_id")
    private String userId;

    @Column(name = "photoYn")
    private Boolean photoYn;

    @Column(name = "point")
    private int point;

    @Column(name = "saved_point")
    private int savedPoint;

    @Column(name = "action")
    private String action;

    public static PointLog createPointLog(EventReviewReq req, int point, int savedPoint) {
        return PointLog.builder()
                .reviewId(req.getReviewId())
                .userId(req.getUserId())
                .photoYn(!req.getAttachedPhotoIds().isEmpty())
                .point(point)
                .savedPoint(savedPoint)
                .action(req.getAction())
                .build();
    }

    public static PointLog createDeletePointLog(EventReviewReq req, int point, int savedPoint) {
        return PointLog.builder()
                .reviewId(req.getReviewId())
                .userId(req.getUserId())
                .photoYn(!req.getAttachedPhotoIds().isEmpty())
                .point(point)
                .savedPoint(savedPoint)
                .action(req.getAction())
                .build();
    }
}
