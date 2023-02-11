package com.kei.reviewservice.business.point.service;

import com.kei.reviewservice.business.point.dto.response.PointRes;
import com.kei.reviewservice.business.point.entity.Point;
import com.kei.reviewservice.business.point.entity.PointLog;
import com.kei.reviewservice.business.point.entity.PointLogRepository;
import com.kei.reviewservice.business.point.entity.PointRepository;
import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.business.review.entity.Review;
import com.kei.reviewservice.business.review.entity.ReviewRepository;
import com.kei.reviewservice.business.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PointServiceImpl implements PointService {

    private PointRepository pointRepository;
    private PointLogRepository logRepository;
    private ReviewRepository reviewRepository;

    public PointServiceImpl(PointRepository pointRepository, PointLogRepository logRepository, ReviewRepository reviewRepository) {
        this.pointRepository = pointRepository;
        this.logRepository = logRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void createPoint(User user) {
        final Point point = Point.createPoint(user);
        pointRepository.save(point);
    }

    @Transactional
    @Override
    public PointRes pointEvent(EventReviewReq req) {
        Point point = null;
        if (req.getAction().equals("ADD"))
            point = addPoint(req);
        else if (req.getAction().equals("MOD"))
            point = modPoint(req);
        else
            point = deletePoint(req);

        PointRes pointRes = new PointRes();
        pointRes.setUserId(req.getUserId());
        pointRes.setPoint(point.getPoint());
        return pointRes;
    }

    private Point addPoint(EventReviewReq req) {
        final Optional<PointLog> logEntity = logRepository.findByReviewIdAndDeleteYn(req.getReviewId(), false);
        if (logEntity.isPresent()) {
            throw new IllegalArgumentException("이미 리뷰 포인트를 받았습니다.");
        }

        final List<Review> reviews =
                reviewRepository.findAllByPlace_PlaceIdAndDeleteYn(req.getPlaceId(), false);

        int point = reviews.isEmpty() ? 2 : 1;

        if (!req.getAttachedPhotoIds().isEmpty())
            point++;

        final PointLog pointLog = PointLog.createPointLog(req, point);
        logRepository.save(pointLog);

        final Point pointEntity = findByUserId(req.getUserId());
        pointEntity.plusPoint(point);
        return pointEntity;
    }

    private Point modPoint(EventReviewReq req) {
        final PointLog pointLog =
                logRepository.findAllByReviewIdAndDeleteYnOrderByIdDesc(req.getReviewId(), false).get(0);

        int point = 0;

        if (req.getAttachedPhotoIds().isEmpty() && pointLog.getPhotoYn()) {
            point -= 1;
            final PointLog newPointLog = PointLog.createPointLog(req, point);
            logRepository.save(newPointLog);

            final Point pointEntity = findByUserId(req.getUserId());
            pointEntity.minusPoint(point);
            return pointEntity;

        } else if (!req.getAttachedPhotoIds().isEmpty() && !pointLog.getPhotoYn()) {
            point += 1;
            final PointLog newPointLog = PointLog.createPointLog(req, point);
            logRepository.save(newPointLog);

            final Point pointEntity = findByUserId(req.getUserId());
            pointEntity.plusPoint(point);
            return pointEntity;
        }

        return findByUserId(req.getUserId());
    }

    private Point deletePoint(EventReviewReq req) {
        final Point point = findByUserId(req.getUserId());
        final List<PointLog> pointLog =
                logRepository.findAllByReviewIdAndDeleteYnOrderByIdDesc(req.getReviewId(), false);

        int savedPoint = pointLog.get(0).getPoint();
        point.minusPoint(savedPoint);

        final PointLog newPointLog = PointLog.createPointLog(req, -savedPoint);
        logRepository.save(newPointLog);

        return point;
    }

    private Point findByUserId(String userId) {
        return pointRepository.findByUserIdAndDeleteYn(userId, false).orElseThrow(
                () -> new IllegalArgumentException("해당 유저의 포인트 내역을 조회할 수 없습니다. ID: " + userId)
        );
    }
}
