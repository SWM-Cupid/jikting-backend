package com.cupid.jikting.member.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.member.dto.SignupRequest;
import com.cupid.jikting.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String USERNAME = "아이디";
    private static final String PASSWORD = "비밀번호";
    private static final String NAME = "이름";
    private static final String PHONE = "전화번호";

    private SignupRequest signupRequest;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        signupRequest = SignupRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .name(NAME)
                .phone(PHONE)
                .build();
    }

    @Test
    void 회원가입_성공() throws Exception {
        // given
        willDoNothing().given(memberService).signup(any(SignupRequest.class));
        // when
        ResultActions resultActions = 회원가입_요청(signupRequest);
        // then
        회원가입_요청_성공(resultActions);
    }

    @Test
    void 회원가입_실패() throws Exception {
        // given
        willThrow(new BadRequestException(ApplicationError.INVALID_FORMAT)).given(memberService).signup(any(SignupRequest.class));
        // when
        ResultActions resultActions = 회원가입_요청(signupRequest);
        // then
        회원가입_요청_실패(resultActions);
    }

    private ResultActions 회원가입_요청(SignupRequest signupRequest) throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + "/members")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(signupRequest)));
    }

    private void 회원가입_요청_성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(toDocument("signup-success"));
    }

    private void 회원가입_요청_실패(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(toDocument("signup-fail"));
    }
}
