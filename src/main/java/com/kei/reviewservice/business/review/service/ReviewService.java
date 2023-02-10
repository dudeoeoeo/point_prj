package com.kei.reviewservice.business.review.service;

import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.business.review.dto.request.ReviewReq;
import com.kei.reviewservice.business.review.dto.request.UpdateReviewReq;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {

    EventReviewReq createReview(List<MultipartFile> files, ReviewReq req, String token);
    EventReviewReq updateReview(String reviewId, List<MultipartFile> files, UpdateReviewReq req, String token);
    EventReviewReq deleteReview(String reviewId, String token);
}
