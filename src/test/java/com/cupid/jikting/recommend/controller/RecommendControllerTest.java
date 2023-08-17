package com.cupid.jikting.recommend.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.TestSecurityConfig;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.jwt.service.JwtService;
import com.cupid.jikting.member.entity.*;
import com.cupid.jikting.recommend.dto.MemberResponse;
import com.cupid.jikting.recommend.dto.RecommendResponse;
import com.cupid.jikting.recommend.entity.Recommend;
import com.cupid.jikting.recommend.service.RecommendService;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamMember;
import com.cupid.jikting.team.entity.TeamPersonality;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(RecommendController.class)
public class RecommendControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/v1";
    private static final String DOMAIN_ROOT_PATH = "/recommends";
    private static final String PATH_DELIMITER = "/";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final Long ID = 1L;
    private static final String NAME = "이름";
    private static final String NICKNAME = "닉네임";
    private static final String HOBBY = "취미";
    private static final String PERSONALITY = "성격";
    private static final String URL = "http://test-url";
    private static final String COMPANY = "회사";
    private static final String DESCRIPTION = "소개";
    private static final String ADDRESS = "거주지";
    private static final String COLLEGE = "대학";
    private static final LocalDate BIRTH = LocalDate.of(1997, 9, 11);
    private static final int HEIGHT = 180;

    private String accessToken;
    private List<RecommendResponse> recommendResponses;
    private ApplicationException teamNotFoundException;

    @MockBean
    private RecommendService recommendService;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        List<String> personalities = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> PERSONALITY + n)
                .collect(Collectors.toList());
        MemberProfile tmpMemberProfile = MemberProfile.builder()
                .id(ID)
                .build();
        List<ProfileImage> profileImages = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> ProfileImage.builder()
                        .memberProfile(tmpMemberProfile)
                        .sequence(Sequence.MAIN)
                        .url(URL)
                        .build())
                .collect(Collectors.toList());
        Company company = Company.builder()
                .name(COMPANY)
                .build();
        MemberCompany memberCompany = MemberCompany.builder()
                .company(company)
                .build();
        Member member = Member.builder()
                .memberCompanies(List.of(memberCompany))
                .build();
        Hobby hobby = Hobby.builder()
                .keyword(HOBBY)
                .build();
        Personality personality = Personality.builder()
                .keyword(PERSONALITY)
                .build();
        MemberHobby memberHobby = MemberHobby.builder()
                .hobby(hobby)
                .build();
        MemberPersonality memberPersonality = MemberPersonality.builder()
                .personality(personality)
                .build();
        MemberProfile memberProfile = MemberProfile.builder()
                .member(member)
                .nickname(NICKNAME)
                .member(member)
                .build();
        memberProfile.updateProfile(BIRTH, HEIGHT, Mbti.ENFJ, ADDRESS, Gender.MALE, COLLEGE, SmokeStatus.SMOKING, DrinkStatus.OFTEN, DESCRIPTION,
                List.of(memberPersonality), List.of(memberHobby));
        List<MemberResponse> memberResponses = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> MemberResponse.from(memberProfile))
                .collect(Collectors.toList());
        TeamPersonality teamPersonality = TeamPersonality.builder().personality(personality).build();
        Team team = Team.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .build();
        team.update(DESCRIPTION, List.of(teamPersonality));
        TeamMember.of(true, team, memberProfile);
        Recommend recommend = Recommend.builder()
                .id(ID)
                .from(team)
                .build();
        RecommendResponse recommendResponse = RecommendResponse.from(recommend);
        this.recommendResponses = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> recommendResponse)
                .collect(Collectors.toList());
        teamNotFoundException = new NotFoundException(ApplicationError.TEAM_NOT_FOUND);
        accessToken = jwtService.createAccessToken(ID);
    }

    @WithMockUser
    @Test
    void 추천팀_조회_성공() throws Exception {
        //given
        willReturn(recommendResponses).given(recommendService).get(anyLong());
        //when
        ResultActions resultActions = 추천팀_조회_요청();
        //then
        추천팀_조회_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 추천팀_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(recommendService).get(anyLong());
        //when
        ResultActions resultActions = 추천팀_조회_요청();
        //then
        추천팀_조회_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 호감_보내기_성공() throws Exception {
        //given
        willDoNothing().given(recommendService).sendLike(anyLong());
        //when
        ResultActions resultActions = 호감_보내기_요청();
        //then
        호감_보내기_요청_성공(resultActions);
    }

    @WithMockUser
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
                .contextPath(CONTEXT_PATH)
                .header(AUTHORIZATION, BEARER + accessToken));
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
