package com.cupid.jikting.recommend;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.recommend.controller.RecommendController;
import com.cupid.jikting.recommend.dto.ImageResponse;
import com.cupid.jikting.recommend.dto.MemberResponse;
import com.cupid.jikting.recommend.dto.RecommendedTeamResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendController.class)
public class RecommendControllerTest extends ApiDocument {

    private final String CONTEXT_PATH = "/api/v1";
    private final String HOBBY = "취미";
    private final String PERSONALITY = "성격";
    private final String URL = "http://test-url";
    private final String COMPANY = "회사";
    private final String DESCRIPTION = "소개";
    private final String DRINK_STATUS = "안마심";
    private final int age = 20;
    private final int HEIGHT = 180;

    private RecommendedTeamResponse recommendedTeamResponse;

    @MockBean
    private RecommendService recommendService;

    @BeforeEach
    void setUp() {
        List<String> hobbyList = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> HOBBY + n)
                .collect(Collectors.toList());

        List<String> personalityList = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> PERSONALITY + n)
                .collect(Collectors.toList());

        List<ImageResponse> imageResponseList = LongStream.rangeClosed(1, 3)
                .mapToObj(n -> ImageResponse.builder()
                        .isMain(true)
                        .memberId(n)
                        .url(URL + n)
                        .build())
                .collect(Collectors.toList());

        List<MemberResponse> memberResponseList = IntStream.rangeClosed(1, 2)
                .mapToObj(n -> MemberResponse.builder()
                        .age(age)
                        .company(COMPANY)
                        .description(DESCRIPTION)
                        .drinkStatus(DRINK_STATUS)
                        .height(HEIGHT)
                        .hobbies(hobbyList)
                        .images(imageResponseList)
                        .build())
                .collect(Collectors.toList());
        recommendedTeamResponse = RecommendedTeamResponse.builder()
                .teamId(1L)
                .members(memberResponseList)
                .personalities(personalityList)
                .build();
    }

    @Test
    void 추천팀_조회_성공() throws Exception {
        //given
        willReturn(recommendedTeamResponse).given(recommendService).getRecommendedTeam(anyLong());
        //when
        ResultActions resultActions = 추천팀_조회_요청();
        //then
        추천팀_조회_요청_성공(resultActions);
    }

    @Test
    void 추천팀_조회_실패() throws Exception {
        //given
        willThrow(new NotFoundException(ApplicationError.TEAM_NOT_FOUND)).given(recommendService).getRecommendedTeam(anyLong());
        //when
        ResultActions resultActions = 추천팀_조회_요청();
        //then
        추천팀_조회_요청_실패(resultActions);
    }

    private ResultActions 추천팀_조회_요청() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(CONTEXT_PATH + "/recommends/1")
                .contextPath(CONTEXT_PATH));
        return resultActions;
    }

    private void 추천팀_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(recommendedTeamResponse))),
                "get-recommended-team-success");
    }

    private void 추천팀_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest()),
                "get-recommended-team-fail");
    }
}
