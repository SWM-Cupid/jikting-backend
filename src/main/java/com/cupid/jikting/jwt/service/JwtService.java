package com.cupid.jikting.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.JwtException;
import com.cupid.jikting.common.service.RedisConnector;
import com.cupid.jikting.member.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    private final RedisConnector redisConnector;

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
        redisConnector.set(memberProfileId.toString(), reissuedRefreshToken, Duration.ofMillis(refreshTokenExpirationPeriod));
        return reissuedRefreshToken;
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
                .map(refreshToken -> refreshToken.replace(BEARER, REMOVE));
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

    public Long extractValidMemberProfileId(String accessToken) {
        return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                        .build()
                        .verify(accessToken.replace(BEARER, REMOVE))
                        .getClaim(MEMBER_PROFILE_ID_CLAIM)
                        .asLong())
                .orElseThrow(() -> new JwtException(ApplicationError.INVALID_TOKEN));
    }

    public void updateRefreshToken(String username, String refreshToken) {
        redisConnector.set(username, refreshToken, Duration.ofMillis((refreshTokenExpirationPeriod)));
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.info("유효하지 않은 토큰입니다. {}", e.getMessage());
            throw new JwtException(ApplicationError.INVALID_TOKEN);
        }
    }

    public String extractAccessToken(HttpServletRequest request) {
        log.info("extractAccessToken() 호출");
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, REMOVE))
                .filter(this::isTokenBlackList)
                .orElseThrow(() -> new JwtException(ApplicationError.UNAUTHORIZED_MEMBER));
    }

    public Duration getExpirationDuration(String accessToken) {
        return Duration.ofMillis(getExpiration(accessToken).getTime() - getTimeFrom(LocalDateTime.now()));
    }

    private static long getTimeFrom(LocalDateTime now) {
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

    private boolean isTokenBlackList(String accessToken) {
        if(ObjectUtils.isEmpty(redisConnector.checkLogout(accessToken))){
            return true;
        }
        throw new JwtException(ApplicationError.LOGGED_OUT_TOKEN);
    }
}
