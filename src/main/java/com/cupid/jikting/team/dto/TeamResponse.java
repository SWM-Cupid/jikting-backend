package com.cupid.jikting.team.dto;

import com.cupid.jikting.team.entity.Team;
import com.cupid.jikting.team.entity.TeamMember;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamResponse {

    private String description;
    private List<String> keywords;
    private List<MemberResponse> members;

    public static TeamResponse from(Team team) {
        List<MemberResponse> memberResponses = team.getTeamMembers()
                .stream()
                .map(TeamMember::getMemberProfile)
                .map(MemberResponse::from)
                .collect(Collectors.toList());
        return new TeamResponse(
                team.getDescription(),
                team.getPersonalities(),
                memberResponses);
    }
}
