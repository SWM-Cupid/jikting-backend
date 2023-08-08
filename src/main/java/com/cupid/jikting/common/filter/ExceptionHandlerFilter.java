package com.cupid.jikting.common.filter;

import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.JwtException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private static final String ENCODING = "UTF-8";

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException exception) {
            setErrorResponse(response, exception);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ApplicationException exception) {
        response.setStatus(exception.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(ENCODING);
        try {
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.from(exception)));
        } catch (IOException e) {
            log.info("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }
}
