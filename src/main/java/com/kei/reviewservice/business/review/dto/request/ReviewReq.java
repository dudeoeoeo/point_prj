package com.kei.reviewservice.business.review.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReviewReq {

    @NotEmpty
    private Long placeId;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String content;

    private double star = 0;

}
