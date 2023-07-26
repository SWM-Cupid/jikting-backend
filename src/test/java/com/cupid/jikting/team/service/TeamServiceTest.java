package com.cupid.jikting.team.service;

import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
import com.cupid.jikting.team.entity.Team;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    private static final String KEYWORD = "성격 키워드";
    private static final Long ID = 1L;
    private static final String NAME = "이름";
    private static final String DESCRIPTION = "한줄소개";
    private static final int MEMBER_COUNT = 3;
    private static final String INVITATION_URL = "https://jikting.com/teams/" + ID + "/invite";

    private Personality personality;
    private Team team;
    private TeamRegisterRequest teamRegisterRequest;

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PersonalityRepository personalityRepository;

    @BeforeEach
    void setUp() {
        List<Personality> personalities = IntStream.range(0, 3)
                .mapToObj(n -> personality)
                .collect(Collectors.toList());
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
        teamRegisterRequest = TeamRegisterRequest.builder()
                .description(DESCRIPTION)
                .memberCount(MEMBER_COUNT)
                .keywords(List.of(KEYWORD))
                .build();
    }

    @Test
    void 팀_등록_성공() {
        // given
        willReturn(Optional.of(personality)).given(personalityRepository).findByKeyword(anyString());
        willReturn(team).given(teamRepository).save(any(Team.class));
        // when
        TeamRegisterResponse teamRegisterResponse = teamService.register(teamRegisterRequest);
        // then
        assertAll(
                () -> verify(personalityRepository).findByKeyword(anyString()),
                () -> verify(teamRepository).save(any(Team.class)),
                () -> assertThat(teamRegisterResponse.getInvitationUrl()).isEqualTo(INVITATION_URL)
        );
    }
}
