package com.kei.reviewservice.business.user.service;

import com.kei.reviewservice.business.user.dto.request.SignUpReq;
import com.kei.reviewservice.business.user.dto.response.UserDetailDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void createUser(SignUpReq req);
    UserDetailDto getUserDetail(String email);
}
