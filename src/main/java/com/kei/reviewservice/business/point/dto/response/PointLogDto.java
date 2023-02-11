package com.kei.reviewservice.business.point.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointLogDto {

    private int stockPoint; // 적립되는 point
    private int reviewPoint; // 리뷰 작성으로 얻은 point
    private String action; // 로그 목적

}
