package com.cupid.jikting.team.controller;

import com.cupid.jikting.ApiDocument;
import com.cupid.jikting.TestSecurityConfig;
import com.cupid.jikting.common.dto.ErrorResponse;
import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.ApplicationException;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.Mbti;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.entity.ProfileImage;
import com.cupid.jikting.member.entity.Sequence;
import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
import com.cupid.jikting.team.dto.TeamResponse;
import com.cupid.jikting.team.dto.TeamUpdateRequest;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamMember;
import com.cupid.jikting.team.entity.TeamPersonality;
import com.cupid.jikting.team.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(TeamController.class)
public class TeamControllerTest extends ApiDocument {

    private static final String CONTEXT_PATH = "/api/v1";
    private static final String DOMAIN_ROOT_PATH = "/teams";
    private static final String PATH_DELIMITER = "/";
    private static final Long ID = 1L;
    private static final String KEYWORD = "키워드";
    private static final String DESCRIPTION = "한줄 소개";
    private static final String INVITATION_URL = "초대 URL";
    private static final String NICKNAME = "닉네임";
    private static final LocalDate BIRTH = LocalDate.of(1996, 5, 22);
    private static final String URL = "이미지 링크";
    private static final Sequence SEQUENCE = Sequence.MAIN;
    private static final Mbti MBTI = Mbti.ENFP;
    private static final String ADDRESS = "거주지";

    private TeamRegisterRequest teamRegisterRequest;
    private TeamUpdateRequest teamUpdateRequest;
    private TeamRegisterResponse teamRegisterResponse;
    private TeamResponse teamResponse;
    private ApplicationException invalidFormatException;
    private ApplicationException teamNotFoundException;
    private ApplicationException memberNotFoundException;

    @MockBean
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        List<String> keywords = IntStream.rangeClosed(1, 3)
                .mapToObj(n -> KEYWORD + n)
                .collect(Collectors.toList());
        List<TeamPersonality> teamPersonalities = IntStream.range(0, 3)
                .mapToObj(n -> TeamPersonality.builder()
                        .personality(Personality.builder()
                                .keyword(KEYWORD)
                                .build())
                        .build())
                .collect(Collectors.toList());
        List<ProfileImage> images = IntStream.range(0, 3)
                .mapToObj(n -> ProfileImage.builder()
                        .url(URL)
                        .sequence(SEQUENCE)
                        .build())
                .collect(Collectors.toList());
        List<TeamMember> teamMembers = IntStream.range(0, 3)
                .mapToObj(n -> MemberProfile.builder()
                        .nickname(NICKNAME)
                        .profileImages(images)
                        .birth(BIRTH)
                        .mbti(MBTI)
                        .address(ADDRESS)
                        .build())
                .map(memberProfile -> TeamMember.builder()
                        .memberProfile(memberProfile)
                        .build())
                .collect(Collectors.toList());
        Team team = Team.builder()
                .id(ID)
                .description(DESCRIPTION)
                .teamPersonalities(teamPersonalities)
                .teamMembers(teamMembers)
                .build();
        teamRegisterRequest = TeamRegisterRequest.builder()
                .description(DESCRIPTION)
                .keywords(keywords)
                .build();
        teamUpdateRequest = TeamUpdateRequest.builder()
                .description(DESCRIPTION)
                .keywords(keywords)
                .build();
        teamRegisterResponse = TeamRegisterResponse.from(INVITATION_URL);
        teamResponse = TeamResponse.from(team);
        invalidFormatException = new BadRequestException(ApplicationError.INVALID_FORMAT);
        teamNotFoundException = new NotFoundException(ApplicationError.TEAM_NOT_FOUND);
        memberNotFoundException = new NotFoundException(ApplicationError.MEMBER_NOT_FOUND);
    }

    @WithMockUser
    @Test
    void 팀_등록_성공() throws Exception {
        // given
        willReturn(teamRegisterResponse).given(teamService).register(any(TeamRegisterRequest.class));
        // when
        ResultActions resultActions = 팀_등록_요청();
        // then
        팀_등록_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 팀_등록_실패() throws Exception {
        // given
        willThrow(invalidFormatException).given(teamService).register(any(TeamRegisterRequest.class));
        // when
        ResultActions resultActions = 팀_등록_요청();
        // then
        팀_등록_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 팀_참여_성공() throws Exception {
        // given
        willDoNothing().given(teamService).attend(anyLong());
        // when
        ResultActions resultActions = 팀_참여_요청();
        // then
        팀_참여_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 팀_참여_실패() throws Exception {
        // given
        willThrow(teamNotFoundException).given(teamService).attend(anyLong());
        // when
        ResultActions resultActions = 팀_참여_요청();
        // then
        팀_참여_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 팀_조회_성공() throws Exception {
        //given
        willReturn(teamResponse).given(teamService).get(anyLong());
        //when
        ResultActions resultActions = 팀_조회_요청();
        //then
        팀_조회_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 팀_조회_실패() throws Exception {
        //given
        willThrow(teamNotFoundException).given(teamService).get(anyLong());
        //when
        ResultActions resultActions = 팀_조회_요청();
        //then
        팀_조회_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 팀_수정_성공() throws Exception {
        //given
        willDoNothing().given(teamService).update(anyLong(), any(TeamUpdateRequest.class));
        //when
        ResultActions resultActions = 팀_수정_요청();
        //then
        팀_수정_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 팀_수정_실패() throws Exception {
        //given
        willThrow(invalidFormatException).given(teamService).update(anyLong(), any(TeamUpdateRequest.class));
        //when
        ResultActions resultActions = 팀_수정_요청();
        //then
        팀_수정_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 팀_삭제_성공() throws Exception {
        // given
        willDoNothing().given(teamService).delete(anyLong());
        // when
        ResultActions resultActions = 팀_삭제_요청();
        // then
        팀_삭제_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 팀_삭제_실패() throws Exception {
        // given
        willThrow(teamNotFoundException).given(teamService).delete(anyLong());
        // when
        ResultActions resultActions = 팀_삭제_요청();
        // then
        팀_삭제_요청_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 팀원_삭제_성공() throws Exception {
        // given
        willDoNothing().given(teamService).deleteMember(anyLong(), anyLong());
        // when
        ResultActions resultActions = 팀원_삭제_요청();
        // then
        팀원_삭제_요청_성공(resultActions);
    }

    @WithMockUser
    @Test
    void 팀원_삭제_팀정보없음_실패() throws Exception {
        // given
        willThrow(teamNotFoundException).given(teamService).deleteMember(anyLong(), anyLong());
        // when
        ResultActions resultActions = 팀원_삭제_요청();
        // then
        팀원_삭제_요청_팀정보없음_실패(resultActions);
    }

    @WithMockUser
    @Test
    void 팀원_삭제_회원정보없음_실패() throws Exception {
        // given
        willThrow(memberNotFoundException).given(teamService).deleteMember(anyLong(), anyLong());
        // when
        ResultActions resultActions = 팀원_삭제_요청();
        // then
        팀원_삭제_요청_회원정보없음_실패(resultActions);
    }

    private ResultActions 팀_등록_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(teamRegisterRequest)));
    }

    private void 팀_등록_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(teamRegisterResponse))),
                "register-team-success");
    }

    private void 팀_등록_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(invalidFormatException)))),
                "register-team-fail");
    }

    private ResultActions 팀_참여_요청() throws Exception {
        return mockMvc.perform(post(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + "/attend")
                .contextPath(CONTEXT_PATH));
    }

    private void 팀_참여_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "attend-team-success");
    }

    private void 팀_참여_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "attend-team-fail");
    }

    private ResultActions 팀_조회_요청() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID)
                .contextPath(CONTEXT_PATH));
    }

    private void 팀_조회_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(teamResponse))),
                "get-team-success");
    }

    private void 팀_조회_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "get-team-fail");
    }

    private ResultActions 팀_수정_요청() throws Exception {
        return mockMvc.perform(patch(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID)
                .contextPath(CONTEXT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(teamUpdateRequest)));
    }

    private void 팀_수정_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "update-team-success");
    }

    private void 팀_수정_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(invalidFormatException)))),
                "update-team-fail");
    }

    private ResultActions 팀_삭제_요청() throws Exception {
        return mockMvc.perform(delete(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID)
                .contextPath(CONTEXT_PATH));
    }

    private void 팀_삭제_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "delete-team-success");
    }

    private void 팀_삭제_요청_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "delete-team-fail");
    }

    private ResultActions 팀원_삭제_요청() throws Exception {
        return mockMvc.perform(delete(CONTEXT_PATH + DOMAIN_ROOT_PATH + PATH_DELIMITER + ID + "/members" + PATH_DELIMITER + ID)
                .contextPath(CONTEXT_PATH));
    }

    private void 팀원_삭제_요청_성공(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk()),
                "delete-team-member-success");
    }

    private void 팀원_삭제_요청_팀정보없음_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(teamNotFoundException)))),
                "delete-team-member-not-found-team-fail");
    }

    private void 팀원_삭제_요청_회원정보없음_실패(ResultActions resultActions) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(toJson(ErrorResponse.from(memberNotFoundException)))),
                "delete-team-member-not-found-member-fail");
    }
}
