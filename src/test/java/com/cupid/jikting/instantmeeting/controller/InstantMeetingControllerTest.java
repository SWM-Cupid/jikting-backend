package com.cupid.jikting.instantmeeting.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.instantmeeting.dto.InstantMeetingResponse;
import com.cupid.jikting.instantmeeting.service.InstantMeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InstantMeetingController.class)
public class InstantMeetingControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/instants";
    private static final Long ID = 1L;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2023, Month.JUNE, 30, 18, 0);
    private static final String LOCATION = "장소";
    private static final int POINT = 10;

    private List<InstantMeetingResponse> instantMeetingResponses;

    @MockBean
    private InstantMeetingService instantMeetingService;

    @BeforeEach
    void setUp() {
        instantMeetingResponses = IntStream.rangeClosed(0, 3)
                .mapToObj(n -> InstantMeetingResponse.builder()
                        .InstantMeetingId(ID)
                        .InstantMeetingDateTime(LOCAL_DATE_TIME)
                        .location(LOCATION)
                        .point(POINT)
                        .build())
                .collect(Collectors.toList());

    }

    @Test
    void 번개팅_조회_성공() throws Exception {
        //given
        willReturn(instantMeetingResponses).given(instantMeetingService).getAll();
        //when
        ResultActions resultActions = 번개팅_조회_요청();
        //then
        번개팅_조회_요청_성공(resultActions);
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
}
