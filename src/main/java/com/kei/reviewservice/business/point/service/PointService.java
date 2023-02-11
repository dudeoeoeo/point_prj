package com.kei.reviewservice.business.point.service;

import com.kei.reviewservice.business.point.dto.response.PointRes;
import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.business.user.entity.User;

public interface PointService {

    void createPoint(User user);
    PointRes pointEvent(EventReviewReq req);
}
