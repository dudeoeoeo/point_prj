package com.kei.reviewservice.business.review.service;

import com.kei.reviewservice.business.place.entity.Place;
import com.kei.reviewservice.business.place.entity.PlaceRepository;
import com.kei.reviewservice.business.review.constant.ActionType;
import com.kei.reviewservice.business.review.constant.Type;
import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.business.review.dto.request.ReviewReq;
import com.kei.reviewservice.business.review.dto.request.UpdateReviewReq;
import com.kei.reviewservice.business.review.entity.Review;
import com.kei.reviewservice.business.review.entity.ReviewImageRepository;
import com.kei.reviewservice.business.review.entity.ReviewRepository;
import com.kei.reviewservice.business.user.entity.User;
import com.kei.reviewservice.business.user.entity.UserRepository;
import com.kei.reviewservice.common.jwt.TokenProvider;
import com.kei.reviewservice.common.utils.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private ReviewImageRepository reviewImageRepository;
    private UserRepository userRepository;
    private PlaceRepository placeRepository;
    private ImageUtil imageUtil;
    private TokenProvider tokenProvider;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewImageRepository reviewImageRepository,
                             UserRepository userRepository, PlaceRepository placeRepository, ImageUtil imageUtil,
                             TokenProvider tokenProvider)
    {
        this.reviewRepository = reviewRepository;
        this.reviewImageRepository = reviewImageRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.imageUtil = imageUtil;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    @Override
    public EventReviewReq createReview(List<MultipartFile> files, ReviewReq req, String token) {
        final String userId = tokenProvider.getUserIdFromToken(token);
        final Optional<Review> reviewOptional =
                reviewRepository.findByPlaceIdAndUserUserIdAndDeleteYn(req.getPlaceId(), userId, false);

        if (reviewOptional.isPresent())
            throw new IllegalArgumentException("이미 작성한 리뷰가 있습니다.");

        final User user = userRepository.findByUserIdAndDeleteYn(userId, false).get();
        final Place place = placeRepository.getReferenceById(req.getPlaceId());

        final Review review = Review.createReview(req, user, place);

        final List<String> imageNames = imageUtil.uploadImages(files);

        review.addReviewImage(imageNames);

        if (!review.getReviewImages().isEmpty())
            reviewImageRepository.saveAll(review.getReviewImages());

        reviewRepository.save(review);

        final EventReviewReq eventReviewReq = EventReviewReq.builder()
                .type(Type.REVIEW.name())
                .action(ActionType.ADD.name())
                .reviewId(review.getReviewId())
                .content(review.getContent())
                .attachedPhotoIds(review.getReviewImages().isEmpty() ? new ArrayList<>() :
                        review.getReviewImages().stream().map(r -> r.getReviewImageId()).collect(Collectors.toList()))
                .userId(userId)
                .placeId(place.getPlaceId())
                .build();

        return eventReviewReq;
    }

    @Transactional
    @Override
    public EventReviewReq updateReview(String reviewId, List<MultipartFile> files, UpdateReviewReq req, String token) {
        final String userId = tokenProvider.getUserIdFromToken(token);
        final Review review = findByReviewId(reviewId);

        if (!userId.equals(review.getUser().getUserId()))
            throw new IllegalArgumentException("리뷰를 작성한 본인만 수정이 가능합니다.");

        review.updateReview(req);

        final List<String> imageNames = imageUtil.uploadImages(files);
        review.addReviewImage(imageNames);

        review.updateReview(req);

        if (review.getReviewImages() != null && !review.getReviewImages().isEmpty())
            reviewImageRepository.saveAll(review.getReviewImages());

        final EventReviewReq eventReviewReq = EventReviewReq.builder()
                .type(Type.REVIEW.name())
                .action(ActionType.MOD.name())
                .reviewId(review.getReviewId())
                .content(review.getContent())
                .attachedPhotoIds(review.getReviewImages().isEmpty() ? new ArrayList<>() :
                        review.getReviewImages().stream().filter(r -> !r.getDeleteYn()).
                                map(r -> r.getReviewImageId()).collect(Collectors.toList()))
                .userId(userId)
                .placeId(review.getPlace().getPlaceId())
                .build();

        return eventReviewReq;
    }

    @Transactional
    @Override
    public EventReviewReq deleteReview(String reviewId, String token) {
        final String userId = tokenProvider.getUserIdFromToken(token);
        final Review review = findByReviewId(reviewId);

        if (!userId.equals(review.getUser().getUserId()))
            throw new IllegalArgumentException("리뷰를 작성한 본인만 삭제가 가능합니다.");

        review.deleteReview();

        if (review.getReviewImages() != null && !review.getReviewImages().isEmpty())
            reviewImageRepository.saveAll(review.getReviewImages());

        final EventReviewReq eventReviewReq = EventReviewReq.builder()
                .type(Type.REVIEW.name())
                .action(ActionType.DELETE.name())
                .reviewId(review.getReviewId())
                .content(review.getContent())
                .attachedPhotoIds(review.getReviewImages().isEmpty() ? new ArrayList<>() :
                        review.getReviewImages().stream().map(r -> r.getReviewImageId()).collect(Collectors.toList()))
                .userId(userId)
                .placeId(review.getPlace().getPlaceId())
                .build();

        return eventReviewReq;
    }


    private Review findByReviewId(String reviewId) {
        return reviewRepository.findByReviewIdAndDeleteYn(reviewId, false).orElseThrow(
                () -> new IllegalArgumentException("해당 ID의 리뷰를 찾을 수 없습니다. ID: " + reviewId)
        );
    }

    /**
     * 포인트 증감 조건
     * 갔던 장소에 리뷰를 달면 포인트 증감
     * 글만 작성 + 1, 사진까지 포함 + 2
     * 그 장소에 처음 리뷰를 남겼다면 보너스 + 1
     *
     */
    /**
     * 포인트 차감 조건
     * 글과 사진으로 작성한 리뷰에서 사진을 삭제하면 - 1
     * 리뷰 삭제 시 받았던 포인트 -
     */
}
