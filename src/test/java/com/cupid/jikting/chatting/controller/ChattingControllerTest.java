package com.cupid.jikting.chatting.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.chatting.dto.*;
import com.cupid.jikting.chatting.service.ChattingService;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.common.error.WrongFormException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

@WebMvcTest(ChattingController.class)
public class ChattingControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/chattings";
    private static final String PATH_DELIMITER = "/";
    private static final String URL = "이미지 주소";
    private static final Long ID = 1L;
    private static final String PLACE = "미팅장소";
    private static final LocalDateTime DATE = LocalDateTime.of(2023, Month.JUNE, 30, 18, 0);
    private static final String TEAM_NAME = "팀이름";
    private static final String MESSAGE = "메시지";
    private static final String DESCRIPTION = "팀소개";
    private static final String KEYWORD = "키워드";
    private static final String NICKNAME = "닉네임";

    private MeetingConfirmRequest meetingConfirmRequest;
    private List<ChattingRoomResponse> chattingRoomResponses;
    private ChattingRoomDetailResponse chattingRoomDetailResponse;
    private ApplicationException wrongFormException;
    private ApplicationException chattingRoomNotFoundException;

    @MockBean
    private ChattingService chattingService;

    @BeforeEach
    void setUp() {
        List<String> images = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> URL)
                .collect(Collectors.toList());
        List<String> keywords = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> KEYWORD + n)
                .collect(Collectors.toList());
        meetingConfirmRequest = MeetingConfirmRequest.builder()
                .meetingId(ID)
                .place(PLACE)
                .schedule(DATE)
                .build();
        chattingRoomResponses = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> ChattingRoomResponse.builder()
                        .chattingRoomId(ID)
                        .opposingTeamName(TEAM_NAME)
                        .lastMessage(MESSAGE)
                        .images(images)
                        .build())
                .collect(Collectors.toList());
        chattingRoomDetailResponse = ChattingRoomDetailResponse.builder()
                .description(DESCRIPTION)
                .keywords(keywords)
                .members(IntStream.rangeClosed(0, 2)
                        .mapToObj(n -> MemberResponse.builder()
                                .memberId(ID)
                                .image(URL)
                                .nickname(NICKNAME)
                                .build())
                        .collect(Collectors.toList()))
                .build();
        wrongFormException = new WrongFormException(ApplicationError.INVALID_FORMAT);
        chattingRoomNotFoundException = new NotFoundException(ApplicationError.CHATTING_ROOM_NOT_FOUND);
    }

    @Test
    void 채팅방_목록_조회_성공() throws Exception {
        //given
        willReturn(chattingRoomResponses).given(chattingService).getAll();
        //when
        ResultActions resultActions = 채팅방_목록_조회_요청();
        //then
        채팅방_목록_조회_요청_성공(resultActions);
    }

    @Test
    void 채팅방_입장_성공() throws Exception {
        //given
        willReturn(chattingRoomDetailResponse).given(chattingService).get(anyLong());
        //when
        ResultActions resultActions = 채팅방_입장_요청();
        //then
        채팅방_입장_요청_성공(resultActions);
    }

    @Test
    void 채팅방_입장_실패() throws Exception {
        //given
        willThrow(chattingRoomNotFoundException).given(chattingService).get(anyLong());
        //when
        ResultActions resultActions = 채팅방_입장_요청();
        //then
        채팅방_입장_요청_실패(resultActions);
    }

    @Test
    void 미팅_확정_성공() throws Exception {
        //given
        willDoNothing().given(chattingService).confirm(anyLong());
        //when
        ResultActions resultActions = 미팅_확정_요청();
        //then
        미팅_확정_요쳥_성공(resultActions);
    }

    @Test
    void 미팅_확정_양식불일치_실패() throws Exception {
        //given
        willThrow(wrongFormException).given(chattingService).confirm(anyLong());
        //when
        ResultActions resultActions = 미팅_확정_요청();
        //then
        미팅_확정_요청_양식불일치_실패(resultActions);
    }

    @Test
    void 미팅_확정_채팅방정보없음_실패() throws Exception {
        //given
        willThrow(chattingRoomNotFoundException).given(chattingService).confirm(anyLong());
        //when
        ResultActions resultActions = 미팅_확정_요청();
        //then
        미팅_확정_요청_채팅방정보없음_실패(resultActions);
    }

    private ResultActions 채팅방_목록_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .contextPath(CONTEXT_PATH));
    }

    private void 채팅방_목록_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(chattingRoomResponses))),
                "get-chattings-success");
    }

    private ResultActions 채팅방_입장_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID)
                .contextPath(CONTEXT_PATH));
    }

    private void 채팅방_입장_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(chattingRoomDetailResponse))),
                "chatting-room-enter-success");
    }

    private void 채팅방_입장_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(chattingRoomNotFoundException)))),
                "chatting-room-enter-fail");
    }

    private ResultActions 미팅_확정_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + "/confirm")
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(meetingConfirmRequest)));
    }

    private void 미팅_확정_요쳥_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "meeting-confirm-success");
    }

    private void 미팅_확정_요청_양식불일치_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(wrongFormException)))),
                "meeting-confirm-wrong-form-fail");
    }

    private void 미팅_확정_요청_채팅방정보없음_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(chattingRoomNotFoundException)))),
                "meeting-confirm-chatting-room-not-found-fail");
    }
}
