package com.kei.reviewservice.business.point.service;

import com.kei.reviewservice.business.point.dto.response.PointLogDto;
import com.kei.reviewservice.business.point.dto.response.PointLogRes;
import com.kei.reviewservice.business.point.dto.response.PointRes;
import com.kei.reviewservice.business.point.entity.Point;
import com.kei.reviewservice.business.point.entity.PointLog;
import com.kei.reviewservice.business.point.entity.PointLogRepository;
import com.kei.reviewservice.business.point.entity.PointRepository;
import com.kei.reviewservice.business.review.constant.ActionType;
import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.business.review.entity.Review;
import com.kei.reviewservice.business.review.entity.ReviewRepository;
import com.kei.reviewservice.business.user.entity.User;
import com.kei.reviewservice.common.jwt.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PointServiceImpl implements PointService {

    private PointRepository pointRepository;
    private PointLogRepository logRepository;
    private ReviewRepository reviewRepository;
    private TokenProvider tokenProvider;

    public PointServiceImpl(PointRepository pointRepository, PointLogRepository logRepository,
                            ReviewRepository reviewRepository, TokenProvider tokenProvider)
    {
        this.pointRepository = pointRepository;
        this.logRepository = logRepository;
        this.reviewRepository = reviewRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void createPoint(User user) {
        final Point point = Point.createPoint(user);
        pointRepository.save(point);
    }

    @Override
    public PointLogRes getUserPointLog(String token) {
        final String userId = tokenProvider.getUserIdFromToken(token);
        final Point point = findByUserId(userId);
        final List<PointLog> pointLogs = logRepository.findAllByUserIdAndDeleteYn(userId, false);

        if (pointLogs.isEmpty()) {
            return PointLogRes.builder().userPoint(point.getPoint()).list(new ArrayList<>()).build();
        }

        final List<PointLogDto> pointLogDtos = pointLogs.stream().map(
                pl -> PointLogDto.builder()
                        .stockPoint(pl.getPoint())
                        .reviewPoint(pl.getSavedPoint())
                        .action(pl.getAction())
                        .build()
        ).collect(Collectors.toList());

        return PointLogRes.builder().userPoint(point.getPoint()).list(pointLogDtos).build();
    }

    @Transactional
    @Override
    public PointRes pointEvent(EventReviewReq req) {
        Point point;
        if (req.getAction().equals(ActionType.ADD.name()))
            point = addPoint(req);
        else if (req.getAction().equals(ActionType.MOD.name()))
            point = modPoint(req);
        else if (req.getAction().equals(ActionType.DELETE.name()))
            point = deletePoint(req);
        else
            throw new IllegalArgumentException("review action error -> " + req.getAction());

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

        int point = reviews.size() <= 1 ? 2 : 1;

        if (!req.getAttachedPhotoIds().isEmpty())
            point++;

        final PointLog pointLog = PointLog.createPointLog(req, point, point);
        logRepository.save(pointLog);

        final Point pointEntity = findByUserId(req.getUserId());
        pointEntity.savePoint(point);
        return pointEntity;
    }

    private Point modPoint(EventReviewReq req) {
        final List<PointLog> pointLogs = logRepository.findAllByReviewIdAndDeleteYnOrderByIdDesc(req.getReviewId(), false);
        if (pointLogs.isEmpty())
            throw new IllegalArgumentException("기존 작성한 리뷰를 찾을 수 없습니다.");

        PointLog pointLog = pointLogs.get(0);

        int point = 0;

        if (req.getAttachedPhotoIds().isEmpty() && pointLog.getPhotoYn()) {
            point -= 1;
            final PointLog newPointLog = PointLog.createPointLog(req, point, pointLog.getSavedPoint() + point);
            logRepository.save(newPointLog);

            final Point pointEntity = findByUserId(req.getUserId());
            pointEntity.savePoint(point);
            return pointEntity;

        } else if (!req.getAttachedPhotoIds().isEmpty() && !pointLog.getPhotoYn()) {
            point += 1;
            final PointLog newPointLog = PointLog.createPointLog(req, point, pointLog.getSavedPoint() + point);
            logRepository.save(newPointLog);

            final Point pointEntity = findByUserId(req.getUserId());
            pointEntity.savePoint(point);
            return pointEntity;
        }

        return findByUserId(req.getUserId());
    }

    private Point deletePoint(EventReviewReq req) {
        final Point point = findByUserId(req.getUserId());
        final PointLog pointLog =
                logRepository.findAllByReviewIdOrderByIdDesc(req.getReviewId()).get(0);

        if (pointLog.getAction().equals(req.getAction()))
            throw new IllegalArgumentException("이미 삭제된 리뷰입니다.");

        int deletePoint = 0 - pointLog.getSavedPoint();
        point.savePoint(deletePoint);

        final PointLog newPointLog = PointLog.createDeletePointLog(req, deletePoint, deletePoint);
        logRepository.save(newPointLog);

        return point;
    }

    private Point findByUserId(String userId) {
        return pointRepository.findByUserUserIdAndDeleteYn(userId, false).orElseThrow(
                () -> new IllegalArgumentException("해당 유저의 포인트 내역을 조회할 수 없습니다. ID: " + userId)
        );
    }
}
