package com.cupid.jikting.like.service;

import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.like.dto.LikeResponse;
import com.cupid.jikting.member.entity.*;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamLike;
import com.cupid.jikting.team.entity.TeamMember;
import com.cupid.jikting.team.entity.TeamPersonality;
import com.cupid.jikting.team.repository.TeamMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    private static final Long ID = 1L;
    private static final String NAME = "팀이름";
    private static final String KEYWORD = "키워드";
    private static final String URL = "url";
    private static final int YEAR = 1967;
    private static final int MONTH = 5;
    private static final int DATE = 10;
    private static final int HEIGHT = 189;
    private static final Mbti MBTI = Mbti.INTJ;
    private static final String ADDRESS = "주소";
    private static final String COLLEGE = "대학";
    private static final String DESCRIPTION = "한 줄 소개";

    private TeamMember teamMember;
    private List<TeamLike> teamLikes;

    @InjectMocks
    private LikeService likeService;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @BeforeEach
    void setUp() {
        Personality personality = Personality.builder()
                .keyword(KEYWORD)
                .build();
        TeamPersonality teamPersonality = TeamPersonality.builder()
                .personality(personality)
                .build();
        List<ProfileImage> profileImages = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> ProfileImage.builder()
                        .id(ID)
                        .url(URL)
                        .sequence(Sequence.MAIN)
                        .build())
                .collect(Collectors.toList());
        MemberProfile memberProfile = MemberProfile.builder()
                .build();
        memberProfile.updateProfile(LocalDate.of(YEAR, MONTH, DATE), HEIGHT, MBTI, ADDRESS, Gender.MALE, COLLEGE, SmokeStatus.SMOKING, DrinkStatus.OFTEN, DESCRIPTION,
                List.of(MemberPersonality.builder().build()), List.of(MemberHobby.builder().build()), profileImages);
        List<TeamMember> teamMembers = IntStream.rangeClosed(0, 2)
                .mapToObj(n -> TeamMember.builder()
                        .memberProfile(memberProfile)
                        .build())
                .collect(Collectors.toList());
        Team sentTeam = Team
                .builder()
                .name(NAME)
                .teamPersonalities(List.of(teamPersonality))
                .teamMembers(teamMembers)
                .build();
        TeamLike teamLike = TeamLike.builder()
                .id(ID)
                .sentTeam(sentTeam)
                .build();
        Team team = Team.builder()
                .id(ID)
                .receivedTeamLikes(List.of(teamLike))
                .build();
        teamMember = TeamMember.builder()
                .team(team)
                .build();
        teamLikes = List.of(TeamLike.builder()
                .sentTeam(team)
                .build());
    }

    @Test
    void 받은_호감_조회_성공() {
        // given
        willReturn(Optional.of(teamMember)).given(teamMemberRepository).getTeamMemberByMemberProfileId(anyLong());
        // when
        List<LikeResponse> likeResponses = likeService.getAllReceivedLike(ID);
        // then
        assertAll(
                () -> verify(teamMemberRepository).getTeamMemberByMemberProfileId(anyLong()),
                () -> assertThat(likeResponses.size()).isEqualTo(teamLikes.size())
        );
    }

    @Test
    void 받은_호감_조회_실패_팀_없음() {
        // given
        willThrow(new BadRequestException(ApplicationError.NOT_EXIST_REGISTERED_TEAM)).given(teamMemberRepository).getTeamMemberByMemberProfileId(anyLong());
        // when & then
        assertThatThrownBy(() -> likeService.getAllReceivedLike(ID))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ApplicationError.NOT_EXIST_REGISTERED_TEAM.getMessage());
    }
}
