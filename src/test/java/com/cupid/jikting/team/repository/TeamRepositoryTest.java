package com.cupid.jikting.team.repository;

import com.cupid.jikting.common.repository.PersonalityRepository;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamPersonality;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeamRepositoryTest {

    private static final String NAME = "이름";
    private static final int MEMBER_COUNT = 3;
    private static final String DESCRIPTION = "한줄소개";

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PersonalityRepository personalityRepository;

    @Test
    void 팀_저장_성공() {
        // given
        Team team = Team.builder()
                .name(NAME)
                .memberCount(MEMBER_COUNT)
                .description(DESCRIPTION)
                .build();
        List<TeamPersonality> teamPersonalities = personalityRepository.findAll()
                .stream()
                .map(personality -> TeamPersonality.builder()
                        .team(team)
                        .personality(personality)
                        .build())
                .collect(Collectors.toList());
        team.addTeamPersonalities(teamPersonalities);
        // when
        Team savedTeam = teamRepository.save(team);
        // then
        assertAll(
                () -> assertThat(savedTeam.getName()).isEqualTo(NAME),
                () -> assertThat(savedTeam.getMemberCount()).isEqualTo(MEMBER_COUNT),
                () -> assertThat(savedTeam.getDescription()).isEqualTo(DESCRIPTION),
                () -> assertThat(savedTeam.getTeamPersonalities().size()).isEqualTo(teamPersonalities.size())
        );
    }

    @Test
    void 팀_삭제_성공() {
        // given
        Team team = Team.builder()
                .name(NAME)
                .memberCount(MEMBER_COUNT)
                .description(DESCRIPTION)
                .build();
        Team savedTeam = teamRepository.save(team);
        // when
        teamRepository.delete(team);
        // then
        assertThat(teamRepository.findById(savedTeam.getId())).isEmpty();
    }
}
