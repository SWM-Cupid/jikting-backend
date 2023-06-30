package com.cupid.jikting.meeting.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.meeting.dto.TeamProfileResponse;
import com.cupid.jikting.meeting.service.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingController.class)
public class MeetingControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/meetings";
    private static final String LIKE_PATH = "/likes";
    private static final String KEYWORD = "키워드";
    private static final String URL = "http://test-url";
    private static final String NAME = "팀명";

    private List<TeamProfileResponse> teamProfileResponses;
    private ApplicationException teamNotFoundException;

    @MockBean
    private MeetingService meetingService;

    @BeforeEach
    void setUp() {
        List<String> keywords = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> KEYWORD + n)
                .collect(Collectors.toList());
        List<String> imageUrls = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> URL + n)
                .collect(Collectors.toList());
        TeamProfileResponse teamProfileResponse = TeamProfileResponse.builder()
                .name(NAME)
                .keywords(keywords)
                .imageUrls(imageUrls)
                .build();
        teamProfileResponses = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> teamProfileResponse)
                .collect(Collectors.toList());
        teamNotFoundException = new NotFoundException(ApplicationError.TEAM_NOT_FOUND);
    }

    @Test
    void 받은_호감_목록_조회_성공() throws Exception {
        //given
        willReturn(teamProfileResponses).given(meetingService).getReceivedLikes();
        //when
        ResultActions resultActions = 받은_호감_목록_조회_요청();
        //then
        받은_호감_목록_조회_요청_성공(resultActions);
    }

    @Test
    void 받은_호감_목록_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(meetingService).getReceivedLikes();
        //when
        ResultActions resultActions = 받은_호감_목록_조회_요청();
        //then
        받은_호감_목록_조회_요청_실패(resultActions);
    }

    @Test
    void 보낸_호감_목록_조회_성공() throws Exception {
        //given
        willReturn(teamProfileResponses).given(meetingService).getSentLikes();
        //when
        ResultActions resultActions = 보낸_호감_목록_조회_요청();
        //then
        보낸_호감_목록_조회_요청_성공(resultActions);
    }

    @Test
    void 보낸_호감_목록_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(meetingService).getSentLikes();
        //when
        ResultActions resultActions = 보낸_호감_목록_조회_요청();
        //then
        보낸_호감_목록_조회_요청_실패(resultActions);
    }

    private ResultActions 받은_호감_목록_조회_요청() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + LIKE_PATH + "/received")
                .contextPath(CONTEXT_PATH));
        return resultActions;
    }

    private void 받은_호감_목록_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(teamProfileResponses))),
                "get-received-likes-success");
    }

    private void 받은_호감_목록_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "get-received-likes-fail");
    }

    private ResultActions 보낸_호감_목록_조회_요청() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + LIKE_PATH + "/sent")
                .contextPath(CONTEXT_PATH));
        return resultActions;
    }

    private void 보낸_호감_목록_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(teamProfileResponses))),
                "get-sent-likes-success");
    }

    private void 보낸_호감_목록_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "get-sent-likes-fail");
    }
}
