package com.cupid.jikting.team.service;

import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import com.cupid.jikting.team.dto.TeamRegisterRequest;
import com.cupid.jikting.team.dto.TeamRegisterResponse;
import com.cupid.jikting.team.dto.TeamResponse;
import com.cupid.jikting.team.dto.TeamUpdateRequest;
import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamMember;
import com.cupid.jikting.team.repository.PersonalityRepository;
import com.cupid.jikting.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class TeamService {

    private static final boolean LEADER = true;
    private static final String TEAM_URL = "https://jikting.com/teams/";
    private static final String INVITE = "/invite";

    private final TeamRepository teamRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final PersonalityRepository personalityRepository;

    public TeamRegisterResponse register(Long memberProfileId, TeamRegisterRequest teamRegisterRequest) {
        MemberProfile memberProfile = getMemberProfileBy(memberProfileId);
        Team team = Team.builder()
                .name(String.valueOf(UUID.randomUUID()))
                .description(teamRegisterRequest.getDescription())
                .memberCount(teamRegisterRequest.getMemberCount())
                .build();
        team.addTeamPersonalities(getPersonalities(teamRegisterRequest.getKeywords()));
        TeamMember.of(LEADER, team, memberProfile);
        MemberProfile savedMemberProfile = memberProfileRepository.save(memberProfile);
        return TeamRegisterResponse.from(TEAM_URL + savedMemberProfile.getTeam().getId() + INVITE);
    }

    public void attend(Long teamId, Long memberProfileId) {
        MemberProfile memberProfile = getMemberProfileBy(memberProfileId);
        TeamMember.of(!LEADER, getTeamBy(teamId), memberProfile);
        memberProfileRepository.save(memberProfile);
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
                .map(this::getPersonalityBy)
                .collect(Collectors.toList());
    }

    private Personality getPersonalityBy(String keyword) {
        return personalityRepository.findByKeyword(keyword)
                .orElseThrow(() -> new NotFoundException(ApplicationError.PERSONALITY_NOT_FOUND));
    }

    private Team getTeamBy(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.TEAM_NOT_FOUND));
    }

    private MemberProfile getMemberProfileBy(Long memberProfileId) {
        return memberProfileRepository.findById(memberProfileId)
                .orElseThrow(() -> new NotFoundException(ApplicationError.MEMBER_NOT_FOUND));
    }
}
