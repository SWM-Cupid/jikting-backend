package com.cupid.jikting.member.handler;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.repository.JwtRepository;
import com.cupid.jikting.common.jwt.service.JwtService;
import com.cupid.jikting.member.entity.Member;
import com.cupid.jikting.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String MEMBER_PROFILE_ID_HEADER_NAME = "MemberProfileId";

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final JwtRepository jwtRepository;

    @Value("${jwt.refreshToken.expiration}")
    private long refreshTokenExpirationPeriod;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String username = extractUsername(authentication);
        Long memberProfileId = getMemberProfileIdByUsername(username);
        String accessToken = jwtService.issueAccessToken(memberProfileId);
        String refreshToken = jwtService.issueRefreshToken();
        jwtService.setAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtRepository.save(memberProfileId.toString(), refreshToken, Duration.ofMillis(refreshTokenExpirationPeriod));
        response.addHeader(MEMBER_PROFILE_ID_HEADER_NAME, String.valueOf(jwtService.extractMemberProfileId(accessToken)));
        log.info("로그인에 성공하였습니다. 아이디 : {} AccessToken : {}", username, accessToken);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private Long getMemberProfileIdByUsername(String username) {
        return memberRepository.findByUsername(username)
                .map(Member::getMemberProfileId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
    }
}
