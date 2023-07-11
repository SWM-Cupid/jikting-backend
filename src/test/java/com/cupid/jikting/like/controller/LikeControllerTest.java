package com.cupid.jikting.like.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.like.dto.MemberProfileResponse;
import com.cupid.jikting.like.dto.TeamDetailResponse;
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
    private static final String URL = "http://test-url";
    private static final String NAME = "팀명";
    private static final Long ID = 1L;
    private static final String NICKNAME = "닉네임";
    private static final int AGE = 20;
    private static final String MBTI = "mbti";
    private static final String ADDRESS = "거주지";
    private static final String COMPANY = "회사";
    private static final boolean IS_SMOKE = true;
    private static final String DRINK_STATUS = "안마심";
    private static final int HEIGHT = 180;
    private static final String DESCRIPTION = "소개";
    private static final String KEYWORD = "키워드";
    private static final String COLLEGE = "대학";

    private List<LikeResponse> likeResponses;
    private TeamDetailResponse teamDetailResponse;
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
        List<MemberProfileResponse> memberProfileRespons = IntStream.rangeClosed(1, 2)
                .mapToObj(n -> MemberProfileResponse.builder()
                        .nickname(NICKNAME)
                        .image(URL)
                        .age(AGE)
                        .mbti(MBTI)
                        .address(ADDRESS)
                        .company(COMPANY)
                        .isSmoke(IS_SMOKE)
                        .drinkStatus(DRINK_STATUS)
                        .height(HEIGHT)
                        .description(DESCRIPTION)
                        .keywords(keywords)
                        .college(COLLEGE)
                        .build())
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
        teamDetailResponse = TeamDetailResponse.builder()
                .likeId(ID)
                .teamName(NAME)
                .keywords(keywords)
                .members(memberProfileRespons)
                .build();
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
        willDoNothing().given(likeService).rejectLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_거절_요청();
        //then
        받은_호감_거절_요청_성공(resultActions);
    }

    @Test
    void 받은_호감_거절_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).rejectLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_거절_요청();
        //then
        받은_호감_거절_요청_실패(resultActions);
    }

    @Test
    void 팀_상세_조회_성공() throws Exception {
        //given
        willReturn(teamDetailResponse).given(likeService).getTeamDetail(anyLong());
        //when
        ResultActions resultActions = 팀_상세_조회_요청();
        //then
        팀_상세_조회_요청_성공(resultActions);
    }

    @Test
    void 팀_상세_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).getTeamDetail(anyLong());
        //when
        ResultActions resultActions = 팀_상세_조회_요청();
        //then
        팀_상세_조회_요청_실패(resultActions);
    }

    private ResultActions 받은_호감_목록_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/received")
                .contextPath(CONTEXT_PATH));
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
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/sent")
                .contextPath(CONTEXT_PATH));
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
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + "/accept")
                .contextPath(CONTEXT_PATH));
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
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + "/reject")
                .contextPath(CONTEXT_PATH));
    }

    private void 받은_호감_거절_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "reject-like-success");
    }

    private void 받은_호감_거절_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "reject-like-fail");
    }

    private ResultActions 팀_상세_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID)
                .contextPath(CONTEXT_PATH));
    }

    private void 팀_상세_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(teamDetailResponse))),
                "get-team-detail-success");
    }

    private void 팀_상세_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "get-team-detail-fail");
    }
}
