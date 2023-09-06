package com.cupid.jikting.member.handler;

import com.cupid.jikting.common.service.RedisConnector;
import com.cupid.jikting.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private static final String LOGOUT_VALUE = "logout";

    private final JwtService jwtService;
    private final RedisJwtRepository redisJwtRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("logout() 호출");
        String accessToken = jwtService.extractAccessToken(request);
        Long memberProfileId = jwtService.extractMemberProfileId(accessToken);
        redisJwtRepository.set(accessToken, LOGOUT_VALUE, jwtService.getRemainingExpirationDuration(accessToken));
        redisJwtRepository.delete(memberProfileId.toString());
    }
}
