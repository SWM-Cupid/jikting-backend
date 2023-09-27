package com.cupid.jikting.member.handler;

import com.cupid.jikting.common.jwt.service.JwtService;
import com.cupid.jikting.common.repository.JwtRepository;
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
    private final JwtRepository jwtRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = jwtService.extractAccessToken(request);
        Long memberProfileId = jwtService.extractMemberProfileId(accessToken);
        jwtRepository.save(accessToken, LOGOUT_VALUE, jwtService.getRemainingExpirationDuration(accessToken));
        jwtRepository.delete(memberProfileId.toString());
    }
}
