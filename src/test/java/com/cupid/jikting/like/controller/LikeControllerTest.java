package com.cupid.jikting.like.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.TestSecurityConfig;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.jwt.service.JwtService;
import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.like.dto.MemberProfileResponse;
import com.cupid.jikting.like.dto.TeamDetailResponse;
import com.cupid.jikting.like.service.LikeService;
import com.cupid.jikting.member.entity.*;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamLike;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(LikeController.class)
public class LikeControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/v1";
    private static final String DOMAIN_ROOT_PATH = "/likes";
    private static final String PATH_DELIMITER = "/";
    private static final String URL = "http://test-url";
    private static final String NAME = "팀명";
    private static final Long ID = 1L;
    private static final String NICKNAME = "닉네임";
    private static final LocalDate BIRTH = LocalDate.of(1996, 5, 10);
    private static final int AGE = 20;
    private static final String ADDRESS = "거주지";
    private static final String COMPANY = "회사";
    private static final String SMOKE_STATUS = "비흡연";
    private static final String DRINK_STATUS = "자주 마심";
    private static final int HEIGHT = 180;
    private static final String DESCRIPTION = "소개";
    private static final String KEYWORD = "키워드";
    private static final String PERSONALITY = "성격";
    private static final String HOBBY = "취미";
    private static final String COLLEGE = "대학";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final int YEAR = 1967;
    private static final int MONTH = 4;
    private static final int DATE = 19;

    private String accessToken;
    private List<LikeResponse> likeResponses;
    private TeamDetailResponse teamDetailResponse;
    private ApplicationException teamNotFoundException;

    @MockBean
    private LikeService likeService;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        accessToken = jwtService.createAccessToken(ID);
        Hobby hobby = Hobby.builder()
                .keyword(HOBBY)
                .build();
        List<MemberHobby> memberHobbies = IntStream.range(0, 3)
                .mapToObj(n -> MemberHobby.builder()
                        .hobby(hobby)
                        .build())
                .collect(Collectors.toList());
        Personality personality = Personality.builder()
                .keyword(PERSONALITY)
                .build();
        List<MemberPersonality> memberPersonalities = IntStream.range(0, 3)
                .mapToObj(n -> MemberPersonality.builder()
                        .personality(personality)
                        .build())
                .collect(Collectors.toList());
        Company company = Company.builder()
                .id(ID)
                .name(COMPANY)
                .build();
        MemberCompany memberCompany = MemberCompany.builder()
                .id(ID)
                .company(company)
                .build();
        Member member = Member.builder()
                .id(ID)
                .memberCompanies(List.of(memberCompany))
                .build();
        MemberProfile memberProfile = MemberProfile.builder()
                .id(ID)
                .member(member)
                .build();
        ProfileImage profileImage = ProfileImage.builder()
                .id(ID)
                .sequence(Sequence.MAIN)
                .url(URL)
                .build();
        memberProfile.updateProfile(BIRTH, HEIGHT, Mbti.ENFJ, ADDRESS, Gender.MALE, COLLEGE, SmokeStatus.SMOKING, DrinkStatus.OFTEN, DESCRIPTION,
                memberPersonalities, memberHobbies, List.of(profileImage));
        List<MemberProfileResponse> memberProfileResponses = IntStream.rangeClosed(1, 2)
                .mapToObj(n -> MemberProfileResponse.from(memberProfile))
                .collect(Collectors.toList());
        List<TeamPersonality> teamPersonalities = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> TeamPersonality.builder()
                        .personality(personality)
                        .build())
                .collect(Collectors.toList());
        List<ProfileImage> profileImages = IntStream.range(0, 3)
                .mapToObj(n -> ProfileImage.builder()
                        .id(ID)
                        .url(URL)
                        .sequence(Sequence.MAIN)
                        .build())
                .collect(Collectors.toList());
        Team team = Team.builder()
                .name(NAME)
                .build();
        team.addTeamPersonalities(teamPersonalities);
        TeamMember.of(false, team, memberProfile);
        TeamLike teamLike = TeamLike.builder()
                .id(ID)
                .sentTeam(team)
                .receivedTeam(team)
                .build();
        LikeResponse likeResponse = LikeResponse.fromSentTeamLike(teamLike);
        likeResponses = List.of(likeResponse, likeResponse);
        teamDetailResponse = TeamDetailResponse.of(ID, team);
        teamNotFoundException = new NotFoundException(ApplicationError.TEAM_NOT_FOUND);
    }

    @WithMockUser
    @Test
    void 받은_호감_목록_조회_성공() throws Exception {
        //given
        willReturn(likeResponses).given(likeService).getAllReceivedLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_목록_조회_요청();
        //then
        받은_호감_목록_조회_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 받은_호감_목록_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).getAllReceivedLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_목록_조회_요청();
        //then
        받은_호감_목록_조회_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 보낸_호감_목록_조회_성공() throws Exception {
        //given
        willReturn(likeResponses).given(likeService).getAllSentLike(anyLong());
        //when
        ResultActions resultActions = 보낸_호감_목록_조회_요청();
        //then
        보낸_호감_목록_조회_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 보낸_호감_목록_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).getAllSentLike(anyLong());
        //when
        ResultActions resultActions = 보낸_호감_목록_조회_요청();
        //then
        보낸_호감_목록_조회_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 받은_호감_수락_성공() throws Exception {
        //given
        willDoNothing().given(likeService).acceptLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_수락_요청();
        //then
        받은_호감_수락_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 받은_호감_수락_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).acceptLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_수락_요청();
        //then
        받은_호감_수락_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 받은_호감_거절_성공() throws Exception {
        //given
        willDoNothing().given(likeService).rejectLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_거절_요청();
        //then
        받은_호감_거절_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 받은_호감_거절_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).rejectLike(anyLong());
        //when
        ResultActions resultActions = 받은_호감_거절_요청();
        //then
        받은_호감_거절_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 보낸팀_상세_조회_성공() throws Exception {
        //given
        willReturn(teamDetailResponse).given(likeService).getSentTeamDetail(anyLong());
        //when
        ResultActions resultActions = 보낸팀_상세_조회_요청();
        //then
        보낸팀_상세_조회_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 보낸팀_상세_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).getSentTeamDetail(anyLong());
        //when
        ResultActions resultActions = 보낸팀_상세_조회_요청();
        //then
        보낸팀_상세_조회_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 받은팀_상세_조회_성공() throws Exception {
        //given
        willReturn(teamDetailResponse).given(likeService).getReceivedTeamDetail(anyLong());
        //when
        ResultActions resultActions = 받은팀_상세_조회_요청();
        //then
        받은팀_상세_조회_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 받은팀_상세_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(likeService).getReceivedTeamDetail(anyLong());
        //when
        ResultActions resultActions = 받은팀_상세_조회_요청();
        //then
        받은팀_상세_조회_요청_실패(resultActions);
    }

    private ResultActions 받은_호감_목록_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + "/received")
                .contextPath(CONTEXT_PATH)
                .header(AUTHORIZATION, BEARER + accessToken));
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
                .contextPath(CONTEXT_PATH)
                .header(AUTHORIZATION, BEARER + accessToken));
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

    private ResultActions 보낸팀_상세_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + PATH_DELIMITER + "sent")
                .contextPath(CONTEXT_PATH));
    }

    private void 보낸팀_상세_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(teamDetailResponse))),
                "get-team-detail-success");
    }

    private void 보낸팀_상세_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "get-team-detail-fail");
    }

    private ResultActions 받은팀_상세_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + PATH_DELIMITER + "received")
                .contextPath(CONTEXT_PATH));
    }

    private void 받은팀_상세_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(teamDetailResponse))),
                "get-team-detail-success");
    }

    private void 받은팀_상세_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "get-team-detail-fail");
    }
}
