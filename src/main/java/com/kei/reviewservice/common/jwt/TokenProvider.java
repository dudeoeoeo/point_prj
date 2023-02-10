package com.kei.reviewservice.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {

    private Environment env;
    private Algorithm algorithm;

    public TokenProvider(Environment env) {
        this.env = env;
        this.algorithm = Algorithm.HMAC512(env.getProperty("token.secret"));
    }

    public String generateToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .sign(algorithm);
    }

    public VerifyResult validToken(String token) {
        token = replaceToken(token);
        try {
            final DecodedJWT decode = JWT.require(algorithm).build().verify(token);
            return VerifyResult.builder().userId(decode.getSubject()).result(true).build();
        } catch (Exception e) {
            final DecodedJWT decode = JWT.decode(token);
            return VerifyResult.builder().userId(decode.getSubject()).result(false).build();
        }
    }

    public String getUserIdFromToken(String token) {
        token = replaceToken(token);
        try {
            return JWT.require(algorithm).build().verify(token).getSubject();
        } catch (JWTVerificationException e) {
            throw e;
        }
    }

    private String replaceToken(String token) {
        return token.replace(JwtProperties.HEADER_PREFIX, "");
    }
}
