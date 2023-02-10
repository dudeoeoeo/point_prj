package com.kei.reviewservice.business.review.dto.response;

import lombok.Data;

@Data
public class ReviewRes {
    private String id;

    private String placeId;

    private String subject;

    private String content;

    private double star = 0;
}
