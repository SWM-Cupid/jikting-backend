package com.cupid.jikting.team.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.team.dto.MemberResponse;
import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
import com.cupid.jikting.team.dto.TeamResponse;
import com.cupid.jikting.team.service.TeamService;
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

@WebMvcTest(TeamController.class)
public class TeamControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/teams";
    private static final String PATH_DELIMITER = "/";
    private static final Long ID = 1L;
    private static final String KEYWORD = "키워드";
    private static final String DESCRIPTION = "한줄 소개";
    private static final String INVITATION_URL = "초대 URL";
    private static final String NICKNAME = "닉네임";
    private static final String URL = "이미지 링크";
    private static final int AGE = 20;
    private static final String MBTI = "mbti";
    private static final String ADDRESS = "거주지";

    private TeamRegisterRequest teamRegisterRequest;
    private TeamRegisterResponse teamRegisterResponse;
    private TeamResponse teamResponse;
    private ApplicationException invalidFormatException;
    private ApplicationException teamNotFoundException;

    @MockBean
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        List<String> keywords = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> KEYWORD + n)
                .collect(Collectors.toList());
        List<String> images = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> URL + n)
                .collect(Collectors.toList());
        List<MemberResponse> members = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> MemberResponse.builder()
                        .nickname(NICKNAME)
                        .images(images)
                        .age(AGE)
                        .mbti(MBTI)
                        .address(ADDRESS)
                        .build())
                .collect(Collectors.toList());
        teamRegisterRequest = TeamRegisterRequest.builder()
                .description(DESCRIPTION)
                .keywords(keywords)
                .build();
        teamRegisterResponse = TeamRegisterResponse.builder()
                .invitationUrl(INVITATION_URL)
                .build();
        teamResponse = TeamResponse.builder()
                .description(DESCRIPTION)
                .keywords(keywords)
                .members(members)
                .build();
        invalidFormatException = new BadRequestException(ApplicationError.INVALID_FORMAT);
        teamNotFoundException = new NotFoundException(ApplicationError.TEAM_NOT_FOUND);
    }

    @Test
    void 팀_등록_성공() throws Exception {
        // given
        willReturn(teamRegisterResponse).given(teamService).register(any(TeamRegisterRequest.class));
        // when
        ResultActions resultActions = 팀_등록_요청();
        // then
        팀_등록_요청_성공(resultActions);
    }

    @Test
    void 팀_등록_실패() throws Exception {
        // given
        willThrow(invalidFormatException).given(teamService).register(any(TeamRegisterRequest.class));
        // when
        ResultActions resultActions = 팀_등록_요청();
        // then
        팀_등록_요청_실패(resultActions);
    }

    @Test
    void 팀_조회_성공() throws Exception {
        //given
        willReturn(teamResponse).given(teamService).get(anyLong());
        //when
        ResultActions resultActions = 팀_조회_요청();
        //then
        팀_조회_요청_성공(resultActions);
    }

    @Test
    void 팀_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(teamService).get(anyLong());
        //when
        ResultActions resultActions = 팀_조회_요청();
        //then
        팀_조회_요청_실패(resultActions);
    }

    @Test
    void 팀_삭제_성공() throws Exception {
        // given
        willDoNothing().given(teamService).delete(anyLong());
        // when
        ResultActions resultActions = 팀_삭제_요청();
        // then
        팀_삭제_요청_성공(resultActions);
    }

    @Test
    void 팀_삭제_실패() throws Exception {
        // given
        willThrow(teamNotFoundException).given(teamService).delete(anyLong());
        // when
        ResultActions resultActions = 팀_삭제_요청();
        // then
        팀_삭제_요청_실패(resultActions);
    }

    private ResultActions 팀_등록_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(teamRegisterRequest)));
    }

    private void 팀_등록_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(teamRegisterResponse))),
                "register-team-success");
    }

    private void 팀_등록_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(invalidFormatException)))),
                "register-team-fail");
    }

    private ResultActions 팀_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID)
                .contextPath(CONTEXT_PATH));
    }

    private void 팀_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(teamResponse))),
                "get-team-success");
    }

    private void 팀_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "get-team-fail");
    }

    private ResultActions 팀_삭제_요청() throws Exception {
        return mockMvc.perform(delete(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID)
                .contextPath(CONTEXT_PATH));
    }

    private void 팀_삭제_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "delete-team-success");
    }

    private void 팀_삭제_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "delete-team-fail");
    }
}
