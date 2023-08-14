package com.cupid.jikting.jwt.filter;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.error.JwtException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.util.PasswordGenerator;
import com.cupid.jikting.jwt.service.JwtService;
import com.cupid.jikting.member.entity.Member;
import com.cupid.jikting.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private static final String LOGIN_URI = "/login";
    private static final String OAUTH_LOGIN_URL = "/oauth";
    private static final String SIGNUP_URL = "/members";
    private static final String CHECK_URL = "/check";
    private static final String PHONE_VERIFICATION_CODE_URL = "/members/code";
    private static final String PHONE_VERIFICATION_URL = "/members/verification";
    private static final String SEARCH_USERNAME_CODE_URL = "/members/username/search/code";
    private static final String SEARCH_USERNAME_VERIFICATION_URL = "/members/username/search/verification";
    private static final String RESET_PASSWORD_URL = "/members/password/reset";
    private static final List<String> TOKEN_AUTHORIZATION_WHITELIST = List.of(LOGIN_URI, OAUTH_LOGIN_URL, CHECK_URL,
            PHONE_VERIFICATION_URL, PHONE_VERIFICATION_CODE_URL, SEARCH_USERNAME_CODE_URL,
            SEARCH_USERNAME_VERIFICATION_URL, RESET_PASSWORD_URL);

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (jwtAuthenticationNotRequired(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);
        if (refreshToken != null) {
            reissueAccessToken(response, refreshToken);
            return;
        }
        saveAccessTokenAuthentication(request, response, filterChain);
    }

    private boolean jwtAuthenticationNotRequired(HttpServletRequest request) {
        return TOKEN_AUTHORIZATION_WHITELIST.stream()
                .anyMatch(uri -> request.getRequestURI().contains(uri)) ||
                (request.getRequestURI().contains(SIGNUP_URL) && request.getMethod().equals(HttpMethod.POST.name()));
    }

    public void reissueAccessToken(HttpServletResponse response, String refreshToken) {
        String username = jwtService.getUsernameByRefreshToken(refreshToken);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
        jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(member.getMemberProfileId()),
                jwtService.reissueRefreshToken(refreshToken, username));
    }

    public void saveAccessTokenAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("saveAccessTokenAuthentication() 호출");
        Member member = memberRepository.findById(jwtService.extractMemberProfileId(request))
                .orElseThrow(() -> new JwtException(ApplicationError.UNAUTHORIZED_MEMBER));
        saveAuthentication(member);
        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(Member member) {
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals(LOGIN_URI);
    }
}
