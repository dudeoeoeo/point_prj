package com.kei.reviewservice.common.jwt;

public interface JwtProperties {
    String HEADER_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String SUBJECT = "kei_token";
    String CLAIM_USER_ID = "userId";
    int EXPIRATION_TIME = 1000 * 60 * 60;
}
