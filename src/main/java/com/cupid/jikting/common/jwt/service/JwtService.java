package com.cupid.jikting.common.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.JwtException;
import com.cupid.jikting.common.repository.RedisJwtRepository;
import com.cupid.jikting.member.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
@Transactional
@Service
public class JwtService {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String MEMBER_PROFILE_ID_CLAIM = "MemberProfileId";
    private static final String BEARER = "Bearer ";
    private static final String REMOVE = "";

    private final MemberRepository memberRepository;
    private final RedisJwtRepository redisJwtRepository;

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

    public String createAccessToken(Long memberProfileId) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(new Date().getTime() + accessTokenExpirationPeriod))
                .withClaim(MEMBER_PROFILE_ID_CLAIM, memberProfileId)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(new Date().getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String reissueRefreshToken(Long memberProfileId) {
        String reissuedRefreshToken = createRefreshToken();
        redisJwtRepository.set(memberProfileId.toString(), reissuedRefreshToken, Duration.ofMillis(refreshTokenExpirationPeriod));
        return reissuedRefreshToken;
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    public String extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .map(refreshToken -> {
                    validateTokenType(refreshToken);
                    return refreshToken.replace(BEARER, REMOVE);
                })
                .orElse(null);
    }

    public Long extractMemberProfileId(String accessToken) {
        try {
            return JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(MEMBER_PROFILE_ID_CLAIM)
                    .asLong();
        } catch (Exception e) {
            log.info("유효하지 않은 토큰입니다. {}", e.getMessage());
            throw new JwtException(ApplicationError.INVALID_ACCESS_TOKEN);
        }
    }

    public void updateRefreshToken(String username, String refreshToken) {
        redisJwtRepository.set(username, refreshToken, Duration.ofMillis((refreshTokenExpirationPeriod)));
    }

    public void validateRefreshToken(String token, Long memberProfileId) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            if (!redisJwtRepository.existRefreshToken(String.valueOf(memberProfileId))) {
                throw new JwtException(ApplicationError.LOGGED_OUT_TOKEN);
            }
        } catch (Exception e) {
            log.info("유효하지 않은 토큰입니다. {}", e.getMessage());
            throw new JwtException(ApplicationError.INVALID_REFRESH_TOKEN);
        }
    }

    public String extractAccessToken(HttpServletRequest request) {
        log.info("extractAccessToken() 호출");
        return Optional.ofNullable(request.getHeader(accessHeader))
                .map(accessToken -> {
                    validateTokenType(accessToken);
                    return accessToken.replace(BEARER, REMOVE);
                })
                .orElseThrow(() -> new JwtException(ApplicationError.UNAUTHORIZED_MEMBER));
    }

    public Duration getRemainingExpirationDuration(String accessToken) {
        return Duration.ofMillis(getExpiration(accessToken).getTime() - getTimeFrom(LocalDateTime.now()));
    }

    private void validateTokenType(String token) {
        if (!token.startsWith(BEARER)) {
            throw new JwtException(ApplicationError.INVALID_TOKEN_TYPE);
        }
    }

    private long getTimeFrom(LocalDateTime now) {
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    private Date getExpiration(String accessToken) {
        return JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken)
                .getExpiresAt();
    }

    private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    private void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    public void validateAccessTokenInBlackList(String accessToken) {
        if (redisJwtRepository.existAccessToken(accessToken)) {
            throw new JwtException(ApplicationError.UNAUTHORIZED_MEMBER);
        }
    }
}
