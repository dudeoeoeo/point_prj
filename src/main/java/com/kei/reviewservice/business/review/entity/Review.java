package com.kei.reviewservice.business.review.entity;

import com.kei.reviewservice.business.place.entity.Place;
import com.kei.reviewservice.business.review.dto.request.ReviewReq;
import com.kei.reviewservice.business.review.dto.request.UpdateReviewReq;
import com.kei.reviewservice.business.user.entity.User;
import com.kei.reviewservice.common.constant.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "review")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_id", unique = true)
    private String reviewId;

    private String subject;

    private String content;

    private double star;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<ReviewImage> reviewImages;

    public static Review createReview(ReviewReq req, User user, Place place)
    {
        return Review.builder()
                .reviewId(UUID.randomUUID().toString())
                .subject(req.getSubject())
                .content(req.getContent())
                .star(req.getStar())
                .reviewImages(new ArrayList<>())
                .user(user)
                .place(place)
                .build();
    }

    public void addReviewImage(List<String> imageNames) {
        if (imageNames.isEmpty()) return;
        imageNames.forEach(i -> this.reviewImages.add(ReviewImage.builder()
                .reviewImageId(UUID.randomUUID().toString())
                .name(i)
                .review(this)
                .build()));
    }

    public void updateReview(UpdateReviewReq req) {
        this.subject = req.getSubject();
        this.content = req.getContent();
        this.star = req.getStar();

        System.out.println("updateReview => " + req.getRemoveImageIds());

        if (req.getRemoveImageIds() == null || req.getRemoveImageIds().isEmpty()) return;

        this.reviewImages.stream().filter(ri -> req.getRemoveImageIds().contains(ri.getReviewImageId()))
                .forEach(ri -> ri.deleteReviewImage(this.user.getUserId()));
    }

    public void deleteReview() {
        this.setDeleteBy(this.user.getUserId());
        this.setDeleteDt(LocalDateTime.now());
        this.setDeleteYn(true);

        if (this.getReviewImages() != null && !this.getReviewImages().isEmpty()) {
            this.getReviewImages().forEach(ri -> ri.deleteReviewImage(this.user.getUserId()));
        }
    }
}
