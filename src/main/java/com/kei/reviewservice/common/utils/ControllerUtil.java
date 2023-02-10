package com.kei.reviewservice.common.utils;

import com.kei.reviewservice.common.jwt.JwtProperties;

import javax.servlet.http.HttpServletRequest;

public class ControllerUtil {

    public String getToken(HttpServletRequest request) {
        return request.getHeader(JwtProperties.HEADER_STRING);
    }
}
