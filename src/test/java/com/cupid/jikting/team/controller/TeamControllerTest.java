package com.cupid.jikting.team.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
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
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
public class TeamControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/teams";
    private static final String KEYWORD = "키워드";
    private static final String DESCRIPTION = "한줄 소개";
    private static final String INVITATION_URL = "초대 URL";

    private TeamRegisterRequest teamRegisterRequest;
    private TeamRegisterResponse teamRegisterResponse;
    private ApplicationException invalidFormatException;

    @MockBean
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        List<String> keywords = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> KEYWORD + n)
                .collect(Collectors.toList());
        teamRegisterRequest = TeamRegisterRequest.builder()
                .description(DESCRIPTION)
                .keywords(keywords)
                .build();
        teamRegisterResponse = TeamRegisterResponse.builder()
                .invitationUrl(INVITATION_URL)
                .build();
        invalidFormatException = new BadRequestException(ApplicationError.INVALID_FORMAT);
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
}