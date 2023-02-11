package com.kei.reviewservice.business.point.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PointLogRes {
    private Long userPoint; // 현재 user 가 가지고 있는 point
    private List<PointLogDto> list;
}
