package com.cupid.jikting.common.jwt.filter;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.error.JwtException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.jwt.service.JwtService;
import com.cupid.jikting.common.util.PasswordGenerator;
import com.cupid.jikting.member.entity.Member;
import com.cupid.jikting.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String CONTEXT_PATH = "/v1";
    private static final String SIGNUP_URI = "/members";

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Value("${jwt.tokenAuthorizationWhiteList}")
    private List<String> tokenAUthorizationWhiteList;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isNotRequiredJwtAuthentication(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = jwtService.extractAccessToken(request);
        String refreshToken = jwtService.extractRefreshToken(request);
        Long memberProfileId = jwtService.extractMemberProfileId(accessToken);
        if (refreshToken != null) {
            jwtService.validateRefreshToken(refreshToken, memberProfileId);
            reissueAccessToken(response, memberProfileId);
            return;
        }
        jwtService.validateAccessTokenInBlackList(accessToken);
        saveAccessTokenAuthentication(request, response, filterChain, accessToken);
    }

    private void reissueAccessToken(HttpServletResponse response, Long memberProfileId) {
        Member member = memberRepository.findById(memberProfileId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
        jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(member.getMemberProfileId()),
                jwtService.reissueRefreshToken(memberProfileId));
    }

    private void saveAccessTokenAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String accessToken)
            throws ServletException, IOException {
        log.info("saveAccessTokenAuthentication() 호출");
        Member member = memberRepository.findById(jwtService.extractMemberProfileId(accessToken))
                .orElseThrow(() -> new JwtException(ApplicationError.UNAUTHORIZED_MEMBER));
        saveAuthentication(member);
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Member member) {
        String password = member.getPassword();
        if (password == null && member.getSocialType() != null) {
            password = PasswordGenerator.generate();
        }
        if (password == null) {
            throw new BadRequestException(ApplicationError.BAD_MEMBER);
        }
        UserDetails userDetails = User.builder()
                .username(member.getId().toString())
                .password(password)
                .roles(member.getRole().name())
                .build();
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                userDetails, null, authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isNotRequiredJwtAuthentication(HttpServletRequest request) {
        return tokenAUthorizationWhiteList.stream().anyMatch(uri -> request.getRequestURI().contains(uri))
                || (request.getRequestURI().equals(CONTEXT_PATH + SIGNUP_URI) && request.getMethod().equals(HttpMethod.POST.name()));
    }
}