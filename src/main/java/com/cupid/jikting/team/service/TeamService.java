package com.cupid.jikting.team.service;

import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.BadRequestException;
import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
import com.cupid.jikting.team.dto.TeamResponse;
import com.cupid.jikting.team.dto.TeamUpdateRequest;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.repository.PersonalityRepository;
import com.cupid.jikting.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeamService {

    private static final String TEAM_URL = "https://jikting.com/teams/";
    private static final String INVITE = "/invite";

    private final TeamRepository teamRepository;
    private final PersonalityRepository personalityRepository;

    public TeamRegisterResponse register(TeamRegisterRequest teamRegisterRequest) {
        Team team = Team.builder()
                .name(String.valueOf(UUID.randomUUID()))
                .description(teamRegisterRequest.getDescription())
                .memberCount(teamRegisterRequest.getMemberCount())
                .build();
        team.addTeamPersonalities(getPersonalities(teamRegisterRequest.getKeywords()));
        Team savedTeam = teamRepository.save(team);
        return TeamRegisterResponse.from(TEAM_URL + savedTeam.getId() + INVITE);
    }

    public void attend(Long teamId) {
    }

    public TeamResponse get(Long teamId) {
        return null;
    }

    public void update(Long teamId, TeamUpdateRequest teamUpdateRequest) {
    }

    public void delete(Long teamId) {
    }

    public void deleteMember(Long teamId, Long memberId) {
    }

    private List<Personality> getPersonalities(List<String> keywords) {
        return keywords.stream()
                .map(this::getPersonality)
                .collect(Collectors.toList());
    }

    private Personality getPersonality(String keyword) {
        return personalityRepository.findByKeyword(keyword)
                .orElseThrow(() -> new BadRequestException(ApplicationError.PERSONALITY_NOT_FOUND));
    }
}
