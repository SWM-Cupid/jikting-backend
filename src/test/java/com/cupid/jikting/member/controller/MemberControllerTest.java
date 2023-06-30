package com.cupid.jikting.member.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.dto.*;
import com.cupid.jikting.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/members";
    private static final String USERNAME = "아이디";
    private static final String PASSWORD = "비밀번호";
    private static final String NAME = "이름";
    private static final String PHONE = "전화번호";
    private static final String NICKNAME = "닉네임";
    private static final String COMPANY = "직장";
    private static final String IMAGE_URL = "사진 URL";
    private static final int AGE = 21;
    private static final int HEIGHT = 168;
    private static final String GENDER = "성별";
    private static final String ADDRESS = "거주지";
    private static final String MBTI = "MBTI";
    private static final String DRINK_STATUS = "음주여부";
    private static final boolean IS_SMOKE = false;
    private static final String COLLEGE = "출신학교(선택사항 - 없을 시 빈 문자열)";
    private static final String PERSONALITY = "성격";
    private static final String HOBBY = "취미";
    private static final String DESCRIPTION = "한줄 소개(선택사항 - 없을 시 빈 문자열)";
    private static final String SEQUENCE = "순서";

    private SignupRequest signupRequest;
    private MemberUpdateRequest memberUpdateRequest;
    private MemberResponse memberResponse;
    private MemberProfileResponse memberProfileResponse;
    private ApplicationException invalidFormatException;
    private ApplicationException memberNotFoundException;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        List<ImageResponse> images = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> ImageResponse.builder()
                        .url(IMAGE_URL)
                        .sequence(SEQUENCE)
                        .build())
                .collect(Collectors.toList());
        List<String> personalities = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> PERSONALITY + n)
                .collect(Collectors.toList());
        List<String> hobbies = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> HOBBY + n)
                .collect(Collectors.toList());
        signupRequest = SignupRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .name(NAME)
                .phone(PHONE)
                .build();
        memberUpdateRequest = MemberUpdateRequest.builder()
                .nickname(NICKNAME)
                .build();
        memberResponse = MemberResponse.builder()
                .nickname(NICKNAME)
                .company(COMPANY)
                .imageUrl(IMAGE_URL)
                .build();
        memberProfileResponse = MemberProfileResponse.builder()
                .images(images)
                .age(AGE)
                .height(HEIGHT)
                .gender(GENDER)
                .address(ADDRESS)
                .mbti(MBTI)
                .drinkStatus(DRINK_STATUS)
                .isSmoke(IS_SMOKE)
                .college(COLLEGE)
                .personalities(personalities)
                .hobbies(hobbies)
                .description(DESCRIPTION)
                .build();
        invalidFormatException = new BadRequestException(ApplicationError.INVALID_FORMAT);
        memberNotFoundException = new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
    }

    @Test
    void 회원가입_성공() throws Exception {
        // given
        willDoNothing().given(memberService).signup(any(SignupRequest.class));
        // when
        ResultActions resultActions = 회원가입_요청();
        // then
        회원가입_요청_성공(resultActions);
    }

    @Test
    void 회원가입_실패() throws Exception {
        // given
        willThrow(invalidFormatException).given(memberService).signup(any(SignupRequest.class));
        // when
        ResultActions resultActions = 회원가입_요청();
        // then
        회원가입_요청_실패(resultActions);
    }

    @Test
    void 회원조회_성공() throws Exception {
        // given
        willReturn(memberResponse).given(memberService).get(anyLong());
        // when
        ResultActions resultActions = 회원조회_요청();
        // then
        회원조회_요청_성공(resultActions);
    }

    @Test
    void 회원조회_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).get(anyLong());
        // when
        ResultActions resultActions = 회원조회_요청();
        // then
        회원조회_요청_실패(resultActions);
    }

    @Test
    void 회원프로필조회_성공() throws Exception {
        // given
        willReturn(memberProfileResponse).given(memberService).getProfile(anyLong());
        // when
        ResultActions resultActions = 회원프로필조회_요청();
        // then
        회원프로필조회_요청_성공(resultActions);
    }

    @Test
    void 회원프로필조회_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).getProfile(anyLong());
        // when
        ResultActions resultActions = 회원프로필조회_요청();
        // then
        회원프로필조회_요청_실패(resultActions);
    }

    @Test
    void 회원수정_성공() throws Exception {
        // given
        willDoNothing().given(memberService).update(any(MemberUpdateRequest.class));
        // when
        ResultActions resultActions = 회원수정_요청();
        // then
        회원수정_요청_성공(resultActions);
    }

    @Test
    void 회원수정_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(memberService).update(any(MemberUpdateRequest.class));
        // when
        ResultActions resultActions = 회원수정_요청();
        // then
        회원수정_요청_실패(resultActions);
    }

    private ResultActions 회원가입_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(signupRequest)));
    }

    private void 회원가입_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "signup-success");
    }

    private void 회원가입_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(invalidFormatException)))),
                "signup-fail");
    }

    private ResultActions 회원조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .contextPath(CONTEXT_PATH));
    }

    private void 회원조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(memberResponse))),
                "get-member-success");
    }

    private void 회원조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "get-member-fail");
    }

    private ResultActions 회원프로필조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/profile")
                .contextPath(CONTEXT_PATH));
    }

    private void 회원프로필조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(memberProfileResponse))),
                "get-member-profile-success");
    }

    private void 회원프로필조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "get-member-profile-fail");
    }

    private ResultActions 회원수정_요청() throws Exception {
        return mockMvc.perform(patch(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(memberUpdateRequest)));
    }

    private void 회원수정_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "update-member-success");
    }

    private void 회원수정_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "update-member-fail");
    }
}
