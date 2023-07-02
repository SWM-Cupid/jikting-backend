package com.cupid.jikting.like.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.like.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeController.class)
public class LikeControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/likes";
    private static final String PATH_DELIMITER = "/";
    private static final String KEYWORD = "키워드";
    private static final String URL = "http://test-url";
    private static final String NAME = "팀명";
    private static final Long ID = 1L;

    private List<LikeResponse> likeResponses;
    private ApplicationException teamNotFoundException;

    @MockBean
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        List<String> keywords = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> KEYWORD + n)
                .collect(Collectors.toList());
        List<String> imageUrls = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> URL + n)
                .collect(Collectors.toList());
        LikeResponse likeResponse = LikeResponse.builder()
                .likeId(ID)
                .name(NAME)
                .keywords(keywords)
                .imageUrls(imageUrls)
                .build();
        likeResponses = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> likeResponse)
                .collect(Collectors.toList());
        teamNotFoundException = new NotFoundException(ApplicationError.TEAM_NOT_FOUND);
    }

    @Test
    void 받은_호감_목록_조회_성공() throws Exception {
        //given
        willReturn(likeResponses).given(likeService).getAllReceivedLike();
        //when
        ResultActions resultActions = 받은_호감_목록_조회_요청();
        //then
        받은_호감_목록_조회_요청_성공(resultActions);
    }

    @Test
    void 받은_호감_목록_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).getAllReceivedLike();
        //when
        ResultActions resultActions = 받은_호감_목록_조회_요청();
        //then
        받은_호감_목록_조회_요청_실패(resultActions);
    }

    @Test
    void 보낸_호감_목록_조회_성공() throws Exception {
        //given
        willReturn(likeResponses).given(likeService).getAllSentLike();
        //when
        ResultActions resultActions = 보낸_호감_목록_조회_요청();
        //then
        보낸_호감_목록_조회_요청_성공(resultActions);
    }

    @Test
    void 보낸_호감_목록_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).getAllSentLike();
        //when
        ResultActions resultActions = 보낸_호감_목록_조회_요청();
        //then
        보낸_호감_목록_조회_요청_실패(resultActions);
    }

    @Test
    void 받은_호감_수락_성공() throws Exception {
        //given
        willDoNothing().given(likeService).acceptLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_수락_요청();
        //then
        받은_호감_수락_요청_성공(resultActions);
    }

    @Test
    void 받은_호감_수락_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).acceptLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_수락_요청();
        //then
        받은_호감_수락_요청_실패(resultActions);
    }

    @Test
    void 받은_호감_거절_성공() throws Exception {
        //given
        willDoNothing().given(likeService).declineLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_거절_요청();
        //then
        받은_호감_거절_요청_성공(resultActions);
    }

    @Test
    void 받은_호감_거절_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).declineLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_거절_요청();
        //then
        받은_호감_거절_요청_실패(resultActions);
    }

    private ResultActions 받은_호감_목록_조회_요청() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/received")
                .contextPath(CONTEXT_PATH));
        return resultActions;
    }

    private void 받은_호감_목록_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(likeResponses))),
                "get-received-likes-success");
    }

    private void 받은_호감_목록_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "get-received-likes-fail");
    }

    private ResultActions 보낸_호감_목록_조회_요청() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/sent")
                .contextPath(CONTEXT_PATH));
        return resultActions;
    }

    private void 보낸_호감_목록_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(likeResponses))),
                "get-sent-likes-success");
    }

    private void 보낸_호감_목록_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "get-sent-likes-fail");
    }

    private ResultActions 받은_호감_수락_요청() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + "/accept")
                .contextPath(CONTEXT_PATH));
        return resultActions;
    }

    private void 받은_호감_수락_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "accept-like-success");
    }

    private void 받은_호감_수락_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "accept-like-fail");
    }

    private ResultActions 받은_호감_거절_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + "/decline")
                .contextPath(CONTEXT_PATH));
    }

    private void 받은_호감_거절_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "decline-like-success");
    }

    private void 받은_호감_거절_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "decline-like-fail");
    }
}
