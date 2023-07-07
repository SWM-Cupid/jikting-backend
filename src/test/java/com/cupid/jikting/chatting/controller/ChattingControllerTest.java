package com.cupid.jikting.chatting.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.chatting.dto.ChattingResponse;
import com.cupid.jikting.chatting.service.ChattingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChattingController.class)
public class ChattingControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/chattings";
    private static final Long ID = 1L;
    private static final String TEAM_NAME = "팀이름";
    private static final String MESSAGE = "메시지";

    private List<ChattingResponse> chattingResponses;

    @MockBean
    private ChattingService chattingService;

    @BeforeEach
    void setUp() {
        chattingResponses = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> ChattingResponse.builder()
                        .chattingId(ID)
                        .opposingTeamName(TEAM_NAME)
                        .lastMessage(MESSAGE)
                        .build())
                .collect(Collectors.toList());

    }

    @Test
    void 채팅방_목록_조회_성공() throws Exception {
        //given
        willReturn(chattingResponses).given(chattingService).getAll();
        //when
        ResultActions resultActions = 채팅방_목록_조회_요청();
        //then
        채팅방_목록_조회_요청_성공(resultActions);
    }

    private ResultActions 채팅방_목록_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .contextPath(CONTEXT_PATH));
    }

    private void 채팅방_목록_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(chattingResponses))),
                "get-chattings-success");
    }
}
