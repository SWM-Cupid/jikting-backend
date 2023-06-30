package com.cupid.jikting.recommend.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.recommend.dto.ImageResponse;
import com.cupid.jikting.recommend.dto.MemberResponse;
import com.cupid.jikting.recommend.dto.RecommendResponse;
import com.cupid.jikting.recommend.service.RecommendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendController.class)
public class RecommendControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/recommends";
    private static final String PATH_DELIMITER = "/";
    private static final String HOBBY = "취미";
    private static final String PERSONALITY = "성격";
    private static final String URL = "http://test-url";
    private static final String COMPANY = "회사";
    private static final String DESCRIPTION = "소개";
    private static final String DRINK_STATUS = "안마심";
    private static final int AGE = 20;
    private static final int HEIGHT = 180;
    private static final Long ID = 1L;
    private static final boolean TRUE = true;

    private List<RecommendResponse> recommendResponses;
    private ApplicationException teamNotFoundException;

    @MockBean
    private RecommendService recommendService;

    @BeforeEach
    void setUp() {
        List<String> hobbies = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> HOBBY + n)
                .collect(Collectors.toList());
        List<String> personalities = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> PERSONALITY + n)
                .collect(Collectors.toList());
        List<ImageResponse> imageResponses = LongStream.rangeClosed(1, 3)
                .mapToObj(n -> ImageResponse.builder()
                        .isMain(TRUE)
                        .memberId(n)
                        .url(URL + n)
                        .build())
                .collect(Collectors.toList());
        List<MemberResponse> memberResponses = IntStream.rangeClosed(1, 2)
                .mapToObj(n -> MemberResponse.builder()
                        .age(AGE)
                        .company(COMPANY)
                        .description(DESCRIPTION)
                        .drinkStatus(DRINK_STATUS)
                        .height(HEIGHT)
                        .hobbies(hobbies)
                        .images(imageResponses)
                        .build())
                .collect(Collectors.toList());
        RecommendResponse recommendResponse = RecommendResponse.builder()
                .recommendId(ID)
                .members(memberResponses)
                .personalities(personalities)
                .build();
        this.recommendResponses = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> recommendResponse)
                .collect(Collectors.toList());
        teamNotFoundException = new NotFoundException(ApplicationError.TEAM_NOT_FOUND);
    }

    @Test
    void 추천팀_조회_성공() throws Exception {
        //given
        willReturn(recommendResponses).given(recommendService).get();
        //when
        ResultActions resultActions = 추천팀_조회_요청();
        //then
        추천팀_조회_요청_성공(resultActions);
    }

    @Test
    void 추천팀_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(recommendService).get();
        //when
        ResultActions resultActions = 추천팀_조회_요청();
        //then
        추천팀_조회_요청_실패(resultActions);
    }

    @Test
    void 호감_보내기_성공() throws Exception {
        //given
        willDoNothing().given(recommendService).sendLike(anyLong());
        //when
        ResultActions resultActions = 호감_보내기_요청();
        //then
        호감_보내기_요청_성공(resultActions);
    }

    @Test
    void 호감_보내기_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(recommendService).sendLike(anyLong());
        //when
        ResultActions resultActions = 호감_보내기_요청();
        //then
        호감_보내기_요청_실패(resultActions);
    }

    private ResultActions 추천팀_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .contextPath(CONTEXT_PATH));
    }

    private void 추천팀_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(recommendResponses))),
                "get-recommends-success");
    }

    private void 추천팀_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "get-recommends-fail");
    }

    private ResultActions 호감_보내기_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + "/like")
                .contextPath(CONTEXT_PATH));
    }

    private void 호감_보내기_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "send-like-success");
    }

    private void 호감_보내기_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "send-like-fail");
    }
}
