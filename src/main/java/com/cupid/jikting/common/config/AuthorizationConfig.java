package com.cupid.jikting.common.config;

import com.cupid.jikting.common.resolver.AuthorizationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class AuthorizationConfig implements WebMvcConfigurer {

    private final AuthorizationResolver authorizationResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizationResolver);
    }
}
