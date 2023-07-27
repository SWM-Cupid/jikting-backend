package com.cupid.jikting.team.service;

import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamMember;
import com.cupid.jikting.team.repository.PersonalityRepository;
import com.cupid.jikting.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    private static final String KEYWORD = "성격 키워드";
    private static final Long ID = 1L;
    private static final String NAME = "이름";
    private static final String DESCRIPTION = "한줄소개";
    private static final int MEMBER_COUNT = 3;
    private static final boolean LEADER = true;
    private static final String INVITATION_URL = "https://jikting.com/teams/" + ID + "/invite";

    private MemberProfile leader;
    private MemberProfile member;
    private Personality personality;
    private Team team;
    private TeamRegisterRequest teamRegisterRequest;

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MemberProfileRepository memberProfileRepository;

    @Mock
    private PersonalityRepository personalityRepository;

    @BeforeEach
    void setUp() {
        List<Personality> personalities = IntStream.range(0, 3)
                .mapToObj(n -> personality)
                .collect(Collectors.toList());
        leader = MemberProfile.builder()
                .id(ID)
                .build();
        member = MemberProfile.builder()
                .id(ID)
                .build();
        personality = Personality.builder()
                .keyword(KEYWORD)
                .build();
        team = Team.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .memberCount(MEMBER_COUNT)
                .build();
        team.addTeamPersonalities(personalities);
        TeamMember.of(LEADER, team, leader);
        TeamMember.of(!LEADER, team, member);
        teamRegisterRequest = TeamRegisterRequest.builder()
                .description(DESCRIPTION)
                .memberCount(MEMBER_COUNT)
                .keywords(List.of(KEYWORD))
                .build();
    }

    @Test
    void 팀_등록_성공() {
        // given
        willReturn(Optional.of(leader)).given(memberProfileRepository).findById(anyLong());
        willReturn(Optional.of(personality)).given(personalityRepository).findByKeyword(anyString());
        willReturn(leader).given(memberProfileRepository).save(any(MemberProfile.class));
        // when
        TeamRegisterResponse teamRegisterResponse = teamService.register(ID, teamRegisterRequest);
        // then
        assertAll(
                () -> verify(memberProfileRepository).findById(anyLong()),
                () -> verify(personalityRepository).findByKeyword(anyString()),
                () -> verify(memberProfileRepository).save(any(MemberProfile.class)),
                () -> assertThat(teamRegisterResponse.getInvitationUrl()).isEqualTo(INVITATION_URL)
        );
    }

    @Test
    void 팀_등록_실패_회원_없음() {
        // given
        willThrow(new NotFoundException(ApplicationError.MEMBER_NOT_FOUND)).given(memberProfileRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> teamService.register(ID, teamRegisterRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 팀_등록_실패_키워드_없음() {
        // given
        willReturn(Optional.of(leader)).given(memberProfileRepository).findById(anyLong());
        willThrow(new NotFoundException(ApplicationError.PERSONALITY_NOT_FOUND)).given(personalityRepository).findByKeyword(anyString());
        // when & then
        assertThatThrownBy(() -> teamService.register(ID, teamRegisterRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.PERSONALITY_NOT_FOUND.getMessage());
    }

    @Test
    void 팀_참여_성공() {
        // given
        willReturn(Optional.of(member)).given(memberProfileRepository).findById(anyLong());
        willReturn(Optional.of(team)).given(teamRepository).findById(anyLong());
        willReturn(member).given(memberProfileRepository).save(any(MemberProfile.class));
        // when
        teamService.attend(ID, ID);
        // then
        assertAll(
                () -> verify(memberProfileRepository).findById(anyLong()),
                () -> verify(teamRepository).findById(anyLong()),
                () -> verify(memberProfileRepository).save(any(MemberProfile.class))
        );
    }
}
