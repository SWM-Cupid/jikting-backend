package com.cupid.jikting.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.JwtException;
import com.cupid.jikting.member.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class JwtService {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "username";
    private static final String BEARER = "Bearer ";

    private final MemberRepository memberRepository;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.accessToken.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refreshToken.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.accessToken.header}")
    private String accessHeader;

    @Value("${jwt.refreshToken.header}")
    private String refreshHeader;

    public String createAccessToken(String username) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(new Date().getTime() + accessTokenExpirationPeriod))
                .withClaim(USERNAME_CLAIM, username)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(new Date().getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractUsername(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(USERNAME_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            throw new JwtException(ApplicationError.INVALID_TOKEN);
        }
    }

    private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    private void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }
}