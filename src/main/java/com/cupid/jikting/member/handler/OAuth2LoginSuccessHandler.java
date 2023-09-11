package com.cupid.jikting.member.handler;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.jwt.service.JwtService;
import com.cupid.jikting.member.entity.Member;
import com.cupid.jikting.member.entity.Role;
import com.cupid.jikting.member.oauth2.CustomOAuth2User;
import com.cupid.jikting.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String TOKEN_TYPE = "Bearer ";
    private static final String OAUTH_SIGNUP_REDIRECT_URL = "https://jikting.com/signup/kakao";
    private static final String LOGIN_REDIRECT_URL = "https://jikting.com/main";

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        log.info("OAuth2 Login 성공!");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        if (oAuth2User.getRole() == Role.GUEST) {
            String accessToken = jwtService.issueAccessToken(getMemberProfileIdByUsername(oAuth2User.getUsername()));
            response.sendRedirect(OAUTH_SIGNUP_REDIRECT_URL);
            jwtService.setAccessAndRefreshToken(response, accessToken, null);
        } else {
            loginSuccess(response, oAuth2User);
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.issueAccessToken(getMemberProfileIdByUsername(oAuth2User.getUsername()));
        String refreshToken = jwtService.issueRefreshToken();
        jwtService.setAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getUsername(), refreshToken);
        response.sendRedirect(LOGIN_REDIRECT_URL);
    }

    private Long getMemberProfileIdByUsername(String username) {
        return memberRepository.findByUsername(username)
                .map(Member::getMemberProfileId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
    }
}
