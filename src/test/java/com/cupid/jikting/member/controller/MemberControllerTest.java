package com.cupid.jikting.member.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.member.dto.MemberUpdateRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String USERNAME = "아이디";
    private static final String PASSWORD = "비밀번호";
    private static final String NAME = "이름";
    private static final String PHONE = "전화번호";
    private static final String NICKNAME = "닉네임";

    private SignupRequest signupRequest;
    private MemberUpdateRequest memberUpdateRequest;

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
        memberUpdateRequest = MemberUpdateRequest.builder()
                .nickname(NICKNAME)
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
        BadRequestException invalidFormatException = new BadRequestException(ApplicationError.INVALID_FORMAT);
        willThrow(invalidFormatException).given(memberService).signup(any(SignupRequest.class));
        // when
        ResultActions resultActions = 회원가입_요청(signupRequest);
        // then
        회원가입_요청_실패(resultActions, invalidFormatException);
    }

    @Test
    void 회원수정_성공() throws Exception {
        // given
        willDoNothing().given(memberService).update(any(MemberUpdateRequest.class));
        // when
        ResultActions resultActions = 회원수정_요청(memberUpdateRequest);
        // then
        회원수정_요청_성공(resultActions);
    }

    private ResultActions 회원가입_요청(SignupRequest signupRequest) throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + "/members")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(signupRequest)));
    }

    private void 회원가입_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "signup-success");
    }

    private void 회원가입_요청_실패(ResultActions resultActions, ApplicationException invalidFormatException) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(invalidFormatException)))),
                "signup-fail");
    }

    private ResultActions 회원수정_요청(MemberUpdateRequest memberUpdateRequest) throws Exception {
        return mockMvc.perform(patch(CONTEXT_PATH + "/members")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(memberUpdateRequest)));
    }

    private void 회원수정_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "update-member-success");
    }
}
