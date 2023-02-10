package com.kei.reviewservice.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kei.reviewservice.business.user.dto.request.LoginReq;
import com.kei.reviewservice.business.user.dto.response.UserDetailDto;
import com.kei.reviewservice.business.user.service.UserService;
import com.kei.reviewservice.common.jwt.JwtProperties;
import com.kei.reviewservice.common.jwt.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private TokenProvider tokenProvider;
    private UserService userService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider,
                                UserService userService)
    {
        this.userService = userService;
        super.setAuthenticationManager(authenticationManager);
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            final LoginReq loginReq = new ObjectMapper().readValue(request.getInputStream(), LoginReq.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException
    {
        String userName = ((User) authResult.getPrincipal()).getUsername();
        final UserDetailDto userDetail = userService.getUserDetail(userName);

        final String token = tokenProvider.generateToken(userDetail.getUserId());
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.HEADER_PREFIX + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
