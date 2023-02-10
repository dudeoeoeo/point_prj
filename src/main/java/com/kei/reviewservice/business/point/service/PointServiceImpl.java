package com.kei.reviewservice.business.point.service;

import com.kei.reviewservice.business.point.entity.Point;
import com.kei.reviewservice.business.point.entity.PointRepository;
import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.business.review.entity.Review;
import com.kei.reviewservice.business.review.entity.ReviewRepository;
import com.kei.reviewservice.business.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PointServiceImpl implements PointService {

    private PointRepository pointRepository;
    private ReviewRepository reviewRepository;

    public PointServiceImpl(PointRepository pointRepository, ReviewRepository reviewRepository) {
        this.pointRepository = pointRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void createPoint(User user) {
        final Point point = Point.createPoint(user);
        pointRepository.save(point);
    }

    @Transactional
    @Override
    public void pointEvent(EventReviewReq req) {
        final List<Review> reviews = reviewRepository.findAllByPlaceIdAndDeleteYn(req.getPlaceId(), false);

    }


}
