package com.cupid.jikting.meeting.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.TestSecurityConfig;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.meeting.dto.InstantMeetingResponse;
import com.cupid.jikting.meeting.service.InstantMeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(InstantMeetingController.class)
public class InstantMeetingControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/instant-meetings";
    private static final String PATH_DELIMITER = "/";
    private static final Long ID = 1L;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2023, Month.JUNE, 30, 18, 0);
    private static final String LOCATION = "장소";

    private List<InstantMeetingResponse> instantMeetingResponses;
    private ApplicationException instantMeetingAlreadyFullException;

    @MockBean
    private InstantMeetingService instantMeetingService;

    @BeforeEach
    void setUp() {
        instantMeetingResponses = IntStream.rangeClosed(0, 3)
                .mapToObj(n -> InstantMeetingResponse.builder()
                        .instantMeetingId(ID)
                        .instantMeetingDateTime(LOCAL_DATE_TIME)
                        .location(LOCATION)
                        .build())
                .collect(Collectors.toList());
        instantMeetingAlreadyFullException = new BadRequestException(ApplicationError.INSTANT_MEETING_ALREADY_FULL);
    }

    @WithMockUser
    @Test
    void 번개팅_조회_성공() throws Exception {
        //given
        willReturn(instantMeetingResponses).given(instantMeetingService).getAll();
        //when
        ResultActions resultActions = 번개팅_조회_요청();
        //then
        번개팅_조회_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 번개팅_참여_성공() throws Exception {
        //given
        willDoNothing().given(instantMeetingService).attend(anyLong());
        //when
        ResultActions resultActions = 번개팅_참여_요청();
        //then
        번개팅_참여_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 번개팅_참여_실패() throws Exception {
        //given
        willThrow(instantMeetingAlreadyFullException).given(instantMeetingService).attend(anyLong());
        //when
        ResultActions resultActions = 번개팅_참여_요청();
        //then
        번개팅_참여_요청_실패(resultActions);
    }

    private ResultActions 번개팅_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .contextPath(CONTEXT_PATH));
    }

    private void 번개팅_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(instantMeetingResponses))),
                "get-instant-meeting-success");
    }

    private ResultActions 번개팅_참여_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID)
                .contextPath(CONTEXT_PATH));
    }

    private void 번개팅_참여_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "attend-instant-meeting-success");
    }

    private void 번개팅_참여_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(instantMeetingAlreadyFullException)))),
                "attend-instant-meeting-fail");
    }
}
