package com.kei.reviewservice.business.review.entity;

import com.kei.reviewservice.common.constant.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_image")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_image_id", unique = true)
    private String reviewImageId;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public void deleteReviewImage(String userId) {
        this.setDeleteYn(true);
        this.setDeleteDt(LocalDateTime.now());
        this.setDeleteBy(userId);
    }
}
