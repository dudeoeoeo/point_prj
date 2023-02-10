package com.kei.reviewservice.business.point.service;

import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.business.user.entity.User;

public interface PointService {

    void createPoint(User user);
    void pointEvent(EventReviewReq req);
}
