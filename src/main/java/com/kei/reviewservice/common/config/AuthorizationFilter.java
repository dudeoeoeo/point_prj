package com.kei.reviewservice.common.config;

import com.kei.reviewservice.business.user.entity.User;
import com.kei.reviewservice.business.user.entity.UserRepository;
import com.kei.reviewservice.common.jwt.JwtProperties;
import com.kei.reviewservice.common.jwt.TokenProvider;
import com.kei.reviewservice.common.jwt.VerifyResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private TokenProvider tokenProvider;

    public AuthorizationFilter(AuthenticationManager authenticationManager,
                               UserRepository userRepository, TokenProvider tokenProvider)
    {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        final String token = request.getHeader(JwtProperties.HEADER_STRING);
        if (token == null || !token.startsWith(JwtProperties.HEADER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        final VerifyResult result = tokenProvider.validToken(token);
        if (result.isResult()) {
            final User user = userRepository.findByUserIdAndDeleteYn(result.getUserId(), false).orElseThrow(
                    () -> new UsernameNotFoundException("유저를 찾을 수 없습니다 -> " + result.getUserId())
            );

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user.getId(), user, null)
            );
        }

        chain.doFilter(request, response);
    }
}
