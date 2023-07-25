package com.cupid.jikting;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@TestConfiguration
public class TestSecurityConfig {

    private static final String MEMBER_ROOT_URI = "/members";
    private static final String VERIFICATION_URI = MEMBER_ROOT_URI + "/verification";
    private static final String VERIFICATION_CODE_URI = MEMBER_ROOT_URI + "/code";
    private static final String USERNAME_URI = MEMBER_ROOT_URI + "/username";
    private static final String NICKNAME_URI = MEMBER_ROOT_URI + "/nickname";
    private static final String LOGIN_URI = MEMBER_ROOT_URI + "/login";
    private static final String PASSWORD_URI = MEMBER_ROOT_URI + "/password";
    private static final String ALL_PATH = "/**";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .mvcMatchers("/").permitAll()
                .mvcMatchers(HttpMethod.POST, MEMBER_ROOT_URI).permitAll()
                .mvcMatchers(USERNAME_URI + ALL_PATH, NICKNAME_URI + ALL_PATH,
                        PASSWORD_URI + ALL_PATH, LOGIN_URI,
                        VERIFICATION_CODE_URI, VERIFICATION_URI).permitAll()
                .anyRequest().authenticated()
                .and()
                .build();
    }
}
