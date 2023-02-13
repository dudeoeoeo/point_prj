package com.kei.reviewservice.business.review.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ReviewReq {

    @NotNull
    private Long placeId;

    @NotEmpty
    private String subject;

    @NotEmpty
    @Size(min = 1, message = "최소 1자 이상 입력해주세요.")
    private String content;

    private double star = 0;

}
