package com.kei.reviewservice.business.point.service;

import com.kei.reviewservice.business.point.dto.response.PointLogRes;
import com.kei.reviewservice.business.point.dto.response.PointRes;
import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.business.user.entity.User;

public interface PointService {

    void createPoint(User user);
    PointLogRes getUserPointLog(String token);
    PointRes pointEvent(EventReviewReq req);
}
