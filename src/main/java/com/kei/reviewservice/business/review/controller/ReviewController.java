package com.kei.reviewservice.business.review.controller;

import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import com.kei.reviewservice.business.review.dto.request.ReviewReq;
import com.kei.reviewservice.business.review.dto.request.UpdateReviewReq;
import com.kei.reviewservice.business.review.service.ReviewService;
import com.kei.reviewservice.common.utils.ControllerUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController extends ControllerUtil {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<EventReviewReq> createReview(HttpServletRequest request, @Valid @RequestPart(value = "req", required = false) ReviewReq req,
                                                       @RequestPart(value = "files", required = false)List<MultipartFile> files)
    {
        final String token = getToken(request);
        final EventReviewReq response = reviewService.createReview(files, req, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<EventReviewReq> updateReview(HttpServletRequest request, @PathVariable String reviewId,
                                                       @Valid @RequestPart UpdateReviewReq req,
                                                       @RequestPart(value = "files", required = false)List<MultipartFile> files)
    {
        final String token = getToken(request);
        final EventReviewReq response = reviewService.updateReview(reviewId, files, req, token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<EventReviewReq> deleteReview(HttpServletRequest request, @PathVariable String reviewId)
    {
        final String token = getToken(request);
        final EventReviewReq response = reviewService.deleteReview(reviewId, token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
