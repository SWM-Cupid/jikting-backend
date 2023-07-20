package com.cupid.jikting.member.handler;

import com.cupid.jikting.common.error.ApplicationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);
        response.getWriter().write(ApplicationError.NOT_EQUAL_ID_OR_PASSWORD.getMessage());
        log.info("로그인에 실패했습니다. 메시지 : {}", exception.getMessage());
    }
}
