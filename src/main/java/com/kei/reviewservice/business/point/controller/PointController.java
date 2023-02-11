package com.kei.reviewservice.business.point.controller;

import com.kei.reviewservice.business.point.dto.response.PointLogRes;
import com.kei.reviewservice.business.point.service.PointService;
import com.kei.reviewservice.common.utils.ControllerUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/point")
public class PointController extends ControllerUtil {

    private PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity getUserPointLog(HttpServletRequest request) {
        final String token = getToken(request);
        final PointLogRes response = pointService.getUserPointLog(token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
