package com.cupid.jikting.team.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.team.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
public class TeamControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/teams";
    private static final String PATH_DELIMITER = "/";
    private static final Long ID = 1L;

    private ApplicationException teamNotFoundException;

    @MockBean
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        teamNotFoundException = new NotFoundException(ApplicationError.TEAM_NOT_FOUND);
    }

    @Test
    void 팀_참여_성공() throws Exception {
        // given
        willDoNothing().given(teamService).attend(anyLong());
        // when
        ResultActions resultActions = 팀_참여_요청();
        // then
        팀_참여_요청_성공(resultActions);
    }

    @Test
    void 팀_참여_실패() throws Exception {
        // given
        willThrow(teamNotFoundException).given(teamService).attend(anyLong());
        // when
        ResultActions resultActions = 팀_참여_요청();
        // then
        팀_참여_요청_실패(resultActions);
    }

    private ResultActions 팀_참여_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + "/attend")
                .contextPath(CONTEXT_PATH));
    }

    private void 팀_참여_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "attend-team-success");
    }

    private void 팀_참여_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "attend-team-fail");
    }
}
