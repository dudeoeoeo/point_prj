package com.kei.reviewservice.business.point;

import com.kei.reviewservice.business.point.dto.response.PointRes;
import com.kei.reviewservice.business.point.service.PointService;
import com.kei.reviewservice.business.review.dto.request.EventReviewReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/event")
public class EventController {

    private PointService pointService;

    public EventController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping
    public ResponseEntity pointEvent(@Valid @RequestBody EventReviewReq req) {
        final PointRes response = pointService.pointEvent(req);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
