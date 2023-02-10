package com.kei.reviewservice.business.review.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UpdateReviewReq {
    @NotEmpty
    private Long placeId;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String content;

    private double star = 0;

    private List<String> removeImageIds;
}
