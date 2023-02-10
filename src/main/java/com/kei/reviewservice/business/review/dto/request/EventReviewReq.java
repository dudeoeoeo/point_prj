package com.kei.reviewservice.business.review.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventReviewReq {
    private String type;
    private String action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;
}
